# PGE 코드 현대화 결과

## 대상 파일
`pge-project/pge.py`

## 발견된 기술 부채

| 항목 | 위험도 | 내용 |
|------|--------|------|
| 중복 함수 정의 | HIGH | `save_workflow`가 두 번 정의됨. 두 번째 정의가 첫 번째를 덮어쓰는데, `BASE_DIR` 미사용으로 실행 위치에 따라 경로 버그 발생 |
| Prompt caching 미적용 | MEDIUM | 시스템 프롬프트(planner/generator/evaluator)가 매 API 호출마다 재전송. 비용 및 응답 지연 불필요하게 증가 |
| 구버전 모델 | MEDIUM | `claude-opus-4-5` 사용 중. 최신 모델(`claude-opus-4-6`) 로 업데이트 가능 |
| JSON 파싱 오류 처리 없음 | MEDIUM | Claude 응답이 JSON이 아닌 경우 (예: 부연 설명 포함) 비인자 예외 발생 |
| 하드코딩된 task 입력 | LOW | `run_pge("내용을 여기에 입력")` — CLI에서 인자로 전달 불가 |

---

## 현대화 내용 (변경 전 → 변경 후)

### 1. 중복 함수 제거 (버그 수정)

**변경 전:**
```python
def save_workflow(filename: str, data: dict):      # 첫 번째 정의 (BASE_DIR 사용)
    path = BASE_DIR / "workflows" / filename
    ...

def save_workflow(filename: str, data: dict):      # 두 번째 정의 (덮어씀, 버그)
    path = Path(f"workflows/{filename}")           # 상대경로 — 실행 위치 의존
    ...
```

**변경 후:**
```python
def save_workflow(filename: str, data: dict):      # 단일 정의, BASE_DIR 사용
    path = BASE_DIR / "workflows" / filename
    ...
```

---

### 2. Prompt Caching 적용

**변경 전:**
```python
response = client.messages.create(
    model="claude-opus-4-5",
    system=load_prompt("planner"),   # 매번 전체 텍스트 전송
    ...
)
```

**변경 후:**
```python
response = client.messages.create(
    model="claude-opus-4-6",
    system=[{
        "type": "text",
        "text": load_prompt("planner"),
        "cache_control": {"type": "ephemeral"}   # 캐시 적용
    }],
    ...
)
```

> 시스템 프롬프트는 호출마다 변하지 않으므로 `cache_control: ephemeral`로 캐싱.  
> 반복 실행(Generator 재시도 등) 시 토큰 비용 최대 90% 절감.

---

### 3. JSON 파싱 오류 처리 추가

**변경 전:**
```python
return json.loads(response.content[0].text)   # 파싱 실패 시 비인자 예외
```

**변경 후:**
```python
try:
    return json.loads(response.content[0].text)
except json.JSONDecodeError as e:
    raise ValueError(f"Planner 응답 파싱 실패: {e}\n응답: {response.content[0].text}")
```

---

### 4. CLI 인자 지원

**변경 전:**
```python
if __name__ == "__main__":
    run_pge("내용을 여기에 입력")   # 하드코딩
```

**변경 후:**
```python
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="PGE: Plan-Generate-Evaluate 코드 현대화 파이프라인")
    parser.add_argument("task", help="현대화할 작업 내용")
    parser.add_argument("--max-iterations", type=int, default=3, help="최대 반복 횟수 (기본값: 3)")
    args = parser.parse_args()
    run_pge(args.task, args.max_iterations)
```

**사용 예시:**
```bash
python pge.py "pge-project를 MCP 서버로 현대화해줘"
python pge.py "레거시 코드를 Quarkus로 마이그레이션해줘" --max-iterations 5
```

---

## 변경 요약

| 변경 항목 | 유형 |
|-----------|------|
| 중복 `save_workflow` 제거 | 버그 수정 |
| Prompt caching 적용 (planner/generator/evaluator) | 성능/비용 개선 |
| 모델 버전 업데이트 (`claude-opus-4-5` → `claude-opus-4-6`) | 기능 개선 |
| JSON 파싱 오류 처리 추가 | 안정성 개선 |
| `argparse` 기반 CLI 지원 추가 | 사용성 개선 |

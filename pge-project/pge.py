import anthropic
import json
from pathlib import Path

client = anthropic.Anthropic()

BASE_DIR = Path(__file__).parent

def load_prompt(name: str) -> str:
    path = BASE_DIR / "prompts" / f"{name}.md"
    return path.read_text(encoding="utf-8")

def save_workflow(filename: str, data: dict):
    path = BASE_DIR / "workflows" / filename
    path.parent.mkdir(exist_ok=True)
    path.write_text(
        json.dumps(data, indent=2, ensure_ascii=False),
        encoding="utf-8"
    )

def run_planner(task: str) -> dict:
    response = client.messages.create(
        model="claude-opus-4-5",
        max_tokens=8096,
        system=load_prompt("planner"),
        messages=[{"role": "user", "content": task}]
    )
    return json.loads(response.content[0].text)

def run_generator(plan: dict, fail_report: str = None) -> dict:
    content = f"plan.json:\n{json.dumps(plan, ensure_ascii=False)}"
    if fail_report:
        content += f"\nfail_report: {fail_report}"

    response = client.messages.create(
        model="claude-opus-4-5",
        max_tokens=8096,
        system=load_prompt("generator"),
        messages=[{"role": "user", "content": content}]
    )
    return json.loads(response.content[0].text)

def run_evaluator(plan: dict, code: dict) -> dict:
    response = client.messages.create(
        model="claude-opus-4-5",
        max_tokens=8096,
        system=load_prompt("evaluator"),
        messages=[{
            "role": "user",
            "content": f"plan:\n{json.dumps(plan, ensure_ascii=False)}\n"
                      f"code:\n{json.dumps(code, ensure_ascii=False)}"
        }]
    )
    return json.loads(response.content[0].text)

def save_workflow(filename: str, data: dict):
    path = Path(f"workflows/{filename}")
    path.parent.mkdir(exist_ok=True)
    path.write_text(
        json.dumps(data, indent=2, ensure_ascii=False),
        encoding="utf-8"
    )

def run_pge(task: str, max_iterations: int = 3):

    # STEP 1: Planner
    print("📋 Planner 실행 중...")
    plan = run_planner(task)
    save_workflow("plan.json", plan)
    print(f"✅ Planner 완료")

    fail_report = None
    for iteration in range(1, max_iterations + 1):

        # STEP 2: Generator
        print(f"⚙️ Generator 실행 중... (iter {iteration})")
        code = run_generator(plan, fail_report)
        code["iteration"] = iteration
        save_workflow("code.json", code)
        print(f"✅ Generator 완료: {code['changed_files']}")

        # STEP 3: Evaluator
        print(f"🔍 Evaluator 실행 중... (iter {iteration})")
        evaluation = run_evaluator(plan, code)
        evaluation["iteration"] = iteration
        save_workflow("evaluation.json", evaluation)

        if evaluation["status"] == "SUCCESS":
            print(f"✅ SUCCESS - {iteration}회 만에 완료")
            return code

        fail_report = evaluation["suggestion"]
        print(f"❌ FAIL iter {iteration}: {fail_report}")

    print("최대 반복 초과 - 실패")
    return None

if __name__ == "__main__":
    run_pge("내용을 여기에 입력")
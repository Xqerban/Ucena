import os
import re
from pathlib import Path

output_dir = Path.cwd().parent
project_root = output_dir.parent


def extract_methods(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = [line.strip() for line in f.readlines()]

    methods = []

    for line in lines:
        if not line:
            continue

        if any(mod in line for mod in ['public ', 'private ', 'protected ', 'static ']) and "implement" not in line:
            if '(' in line and ')' in line:
                signature_with_mod = line.split('{')[0].strip()

                parts_without_mod = signature_with_mod.split(' ')
                parts_without_mod.pop(0)
                parts_without_mod.pop(0)

                methods.append(' '.join(parts_without_mod))

    return methods


def generate_test_signature(original_sig):
    """Generate test signature from production signature"""
    file_part, method_part = original_sig.split(':')

    # Transform file path
    test_file = file_part.replace('src.main', 'test', 1)

    test_file = "Test.java".join(test_file.rsplit(".java", 1))

    return f"{test_file}:{method_part}"


def process_directory(target_dir):
    """Main processing function"""
    output = []

    for filename in os.listdir(target_dir):
        if filename.endswith('.java'):
            output.append(f"### {filename.split('.')[0]}\n")

            file_path = Path(target_dir) / filename
            rel_path = file_path.relative_to(project_root)
            dotted_path = '.'.join(rel_path.parts)

            methods = extract_methods(file_path)
            for method in methods:
                sig1 = f"{dotted_path}:{method}"
                sig2 = generate_test_signature(sig1)

                output.append(f"**测试对象**：`{sig1}`\n**测试函数**：`{sig2}`\n")
                output.append("| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |\n")
                output.append(
                    "|:--------:|:--------:|:--------:|:--------:|:----:|\n")
                output.append(
                    "|          |          |          |          |      |\n" * 3)
                output.append("\n\n\n")

    return ''.join(output)


if __name__ == '__main__':
    import sys
    if len(sys.argv) != 2:
        print("Usage: python docgen.py <base_directory>")
        sys.exit(1)

    with open(output_dir/"unit-test-report.md", 'w') as f:
        f.write(process_directory(sys.argv[1]))

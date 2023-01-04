from pathlib import Path, PurePath
from io import StringIO
import argparse
import sys
import re
import subprocess

# regex to extract puml contents which is enclosed between """ """
puml_regex = r"(?<=\"{3}\n)[\s\S]*(?=\"{3})"


def extract_puml(contents: str) -> str:
	return re.search(puml_regex, contents, flags=re.MULTILINE).group(0)


def write_puml(f_name: Path, contents: str):
	with open(f_name, "w", encoding="utf-8") as f:
		f.write(contents)
		f.flush()
		f.close()


def read_file(f_name: Path) -> str:
	with open(f_name, encoding="utf-8") as f:
		contents = f.read()
		return contents


def convert_folder(dir_name: Path, extension: str):
	subprocess.call(["plantuml", "-t" + extension, str(dir_name) + "/*.puml"])

def cleanup_puml_files(dir_name: Path):

	for f in dir_name.glob("*.puml"):
		f.unlink()

def prepare_output_dir(args) -> Path:
	doc_dir = args.testdir / 'docs'
	doc_dir.mkdir(parents=True, exist_ok=True)
	return doc_dir


def add_section_header(doc_builder: StringIO, diagram_type: str):
	doc_builder.write("## " + diagram_type + "\n")
	doc_builder.write("\n")
	doc_builder.write("<table>\n")
	doc_builder.write("<tr>\n")
	doc_builder.write("<th>File<br>(.java)</th><th>PlantUML</th><th>Text</th>\n")
	doc_builder.write("</tr>\n")


def finish_section(doc_builder: StringIO):
	doc_builder.write("</table>")
	doc_builder.write("\n\n")


def add_entry(doc_builder: StringIO, file: str, image: str, uml: str):
	doc_builder.write("<tr>")
	add_table_item(doc_builder, file)
	image_str = f"<img alt=\"{file}\" src=\"{image}\" width=\"400\"/>"
	add_table_item(doc_builder, image_str)


	fixed = uml.replace("<<", "&laquo;")
	fixed = fixed.replace(">>", "&raquo;")
	uml_str = f"<pre><code>{fixed}</code></pre>"
	add_table_item(doc_builder, uml_str)
	doc_builder.write("</tr>")
	doc_builder.write("\n")


def add_table_item(doc_builder: StringIO, item: str):
	doc_builder.write("<td>")
	doc_builder.write(item)
	doc_builder.write("</td>")


def output_test_overview(f_name: Path, contents: str):
	with open(f_name, 'w', encoding='utf-8') as f:
		f.write(contents)
		f.flush()
		f.close()


def process(args):
	doc_builder = StringIO()
	doc_builder.write("# Plantuml GraphML Export Test Case Overview\n")
	doc_builder.write("\n")

	doc_dir = prepare_output_dir(args)

	for directory in list(filter(lambda f: f.is_dir(), args.testdir.glob('*'))):

		# ignore the output dir created
		if directory.stem == "docs":
			continue

		add_section_header(doc_builder, directory.stem)
		folder = doc_dir / directory.stem
		folder.mkdir(parents=True, exist_ok=True)

		for file in sorted(directory.glob('GML*_Test.java')):
			contents = read_file(file)
			puml = extract_puml(contents)
			puml_file = (folder / file.name).with_suffix('.puml')
			write_puml(puml_file, puml)
			add_entry(doc_builder, file.stem, "./" + directory.stem + "/" + file.stem + ".svg", puml)
		finish_section(doc_builder)

		convert_folder(folder, "svg")
		cleanup_puml_files(folder)

	output_test_overview(doc_dir / "test_overview.md", doc_builder.getvalue())


# Define arguments
parser = argparse.ArgumentParser()
parser.add_argument("testdir", type=Path, help="the directory with test files to process")

if __name__ == "__main__":

	args = parser.parse_args()
	if len(sys.argv) == 1:
		parser.print_help()
	else:
		process(args)

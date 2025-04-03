import os
import re

def process_file(filepath):
    with open(filepath, 'r') as file:
        content = file.read()

    # Extracting the elements to variables
    running_time = re.search(r'\*\*Running Time:\*\* (.+?)\n', content)
    budget = re.search(r'\*\*Budget:\*\* (.+?)\n', content)
    box_office = re.search(r'\*\*Box Office:\*\* (.+?)\n', content)
    country = re.search(r'\*\*Country:\*\* (.+?)\n', content)
    plot = re.search(r'\*\*Plot:\*\* (.+)', content, re.DOTALL)
    image_link = re.search(r'!\[.*?\]\((.+?)\)', content)
    existing_variables = re.search(r'^---\n(.*?)\n---\n', content, re.DOTALL)

    # Creating the variables section
    variables = []
    if existing_variables:
        variables.append(f'---\n{existing_variables.group(1)}')
    if image_link:
        variables.append(f'poster: "{image_link.group(1)}"')
    if running_time:
        variables.append(f'running_time: "{running_time.group(1)}"')
    if budget:
        variables.append(f'budget: "{budget.group(1)}"')
    if box_office:
        variables.append(f'box_office: "{box_office.group(1)}"')
    if country:
        variables.append(f'country: "{country.group(1)}"')
    if plot:
        var = plot.group(1).strip().replace("\n", "\\n").replace("\"", "\\\"")
        variables.append(f'plot: "{var}"')
    variables_section = "\n".join(variables) + "\n---\n"
    print(variables_section)
    # Write the variables section back to the file
    with open(filepath, 'w') as file:
        file.write(variables_section)

def process_directory(directory):
    print(directory)
    for root, _, files in os.walk(directory):
        print(files)
        for file in files:
            if file.endswith('.md'):
                print(file)
                process_file(os.path.join(root, file))

if __name__ == "__main__":
    process_directory(".")
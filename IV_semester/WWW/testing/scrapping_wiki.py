import requests
from bs4 import BeautifulSoup
url = "https://en.wikipedia.org/wiki/Reservoir_Dogs"

response = requests.get(url)

response.raise_for_status()

soup = BeautifulSoup(response.text, 'html.parser')
plot_section = soup.find('h2', id="Plot")

sibling = plot_section.find_next()
# print(sibling)
plot_content = ""
while(sibling.get("id") != "Cast"):
    if sibling.name =="p":
        plot_content += sibling.text
        plot_content += "\n"
    sibling = sibling.find_next()
print(plot_content)


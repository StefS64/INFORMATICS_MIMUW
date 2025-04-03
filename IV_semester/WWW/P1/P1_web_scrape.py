import requests
from bs4 import BeautifulSoup
import numpy as np
import os
import time
import csv
from duckduckgo_search import DDGS
from googlesearch import search
import re
import random

def remove_brackets(text):
    return re.sub(r'\[.*?\]', '', text)

def simplyfy(title):
    return re.sub(r'[\W_]+', '-', title.lower()).strip('-')

def create_subpage(film_additional_info):
    path_movie_dir = 'film_site/additional_info'

    if not os.path.exists(path_movie_dir):
        os.makedirs(path_movie_dir)
    simple_title = simplyfy(film_additional_info['Title'])
    print(simple_title)
    subsite_file_path = os.path.join(f"film_site/additional_info/{simple_title}.md")
    permalink = f"/additional_info/{simple_title}/"
    subsite_content = f"""---
layout: additional_movie_info
title: "{film_additional_info['Title']}"
permalink: {permalink}
---

![{film_additional_info['Title']}]({film_additional_info['Poster']})

**Running Time:** {remove_brackets(film_additional_info['Running Time'])}

**Budget:** {remove_brackets(film_additional_info['Budget'])}

**Box Office:** {remove_brackets(film_additional_info['Box Office'])}

**Country:** {remove_brackets(film_additional_info['Country'])}

**Plot:** {film_additional_info['Plot']}"""
    with open(subsite_file_path, 'w') as f:
        f.write(subsite_content)



def get_more_info(film_name):
    time.sleep(5 + random.uniform(60, 65))
    query = f"{film_name} site:en.wikipedia.org"
    print(f"Searching more info in duckduck:{query}")
    # driver = webdriver.Chrome()
    # search_results = driver.get(f"https://www.google.com/search?q={query}")
    # search_results = list(search(query, num_results=1))
    # ddgs = DDGS(proxy="tb", timeout=20)  # "tb" is an alias for "socks5://127.0.0.1:9150"
    search_results = list(DDGS().text(query, max_results=1))[0]

    site = search_results['href']
    response_info = requests.get(site)
    soup = BeautifulSoup(response_info.content, 'html.parser')

    plot_section = soup.find('h2', id="Plot")

    sibling = plot_section.find_next()
    
    plot_content = ""
    while(sibling.get("id") != "Cast" and sibling.get("id") != "Voice_cast" and sibling.get("id") != "Production"):
        # if sibling.get("id"):
        #     print(sibling.get("id"))
        if sibling.name =="p":
            plot_content += sibling.text
            plot_content += "\n"
        sibling = sibling.find_next()

    infobox = soup.find('table', class_='infobox')
    poster_tag = infobox.find('img') if infobox else None
    poster_url = f"https:{poster_tag['src']}" if poster_tag else 'ERROR'

    data = {
        "Title": film_name,
        "Budget": infobox.find("th", string="Budget").find_next("td").text.strip(),
        "Box Office": infobox.find("th", string="Box office").find_next("td").text.strip(),
        "Running Time": infobox.find("th", string="Running time").find_next("td").text.strip(),
        "Country": infobox.find("th", string="Country").find_next("td").text.strip() if soup.find("th", string="Country") else soup.find("th", string="Countries").find_next("td").text.strip(),
        "Plot": plot_content.strip(),
        "Poster": poster_url
    }
    return data
# Specify the URL
url = "https://www.empireonline.com/movies/features/best-movies-2/"

site_dir = 'film_site'
# Preping jekyll
if not os.path.exists(site_dir):
    os.system('jekyll new film_site')
    os.system('rm ./film_site/_posts/*')

# Getting the films:
response = requests.get(url)
response.raise_for_status()  # Check if the request was successful
soup = BeautifulSoup(response.content, 'html.parser')

movies_data = []

movies = soup.find_all('h2')
for movie in movies[1:]:
    title = movie.get_text(strip=True)
    
    director_tag = movie.find_next('p')
    director = director_tag.get_text(strip=True).replace('Director:', '').strip() if director_tag else 'ERROR'
    
    image_tag = movie.find_next('img')
    image_url = image_tag['src'] if image_tag else 'ERROR'

    starring_tag = director_tag.find_next('p')
    starring = starring_tag.get_text(strip=True).replace('Starring:', '').strip() if starring_tag else 'ERROR'
    
    commentary_tag = starring_tag.find_next('p') if starring_tag else None
    commentary = commentary_tag.get_text(strip=True) if commentary_tag else 'ERROR'

    film_name = re.sub(r'^.*?\)', '', title)
    movies_data.append({
        'title': title,
        'director': director,
        'image_url': image_url,
        'starring': starring,
        'commentary': commentary,
        'url': f"/additional_info/{simplyfy(film_name)}"
    })
    # print(movies_data['url'])

movies_array = np.array(movies_data, dtype=object)

data_dir = os.path.join(site_dir, '_data')
if not os.path.exists(data_dir):
    os.makedirs(data_dir)

csv_file_path = os.path.join(data_dir, 'movies_data.csv')

with open(csv_file_path, 'w', newline='') as csvfile:
    fieldnames = ['title', 'director', 'image_url', 'starring', 'commentary','url']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    writer.writeheader()
    writer.writerows(movies_data)

posts_dir = 'film_site/_posts'

markdown_content = """---
---
layout: post_overide
title: "Top Movies"
---
Top 100 Movies.
"""
scraped_movie_post = os.path.join(posts_dir, '2025-02-27-top-movies-scraped.md')

with open(scraped_movie_post, 'w') as f:
    f.write(markdown_content)

movie_num = int(input("Enter a number: "))
for movie in movies[1+movie_num:]: 
    print(movie_num)
    movie_num += 1
    title = movie.get_text(strip=True)
    additional_info = get_more_info(re.sub(r'^.*?\)', '', title))
    create_subpage(additional_info)
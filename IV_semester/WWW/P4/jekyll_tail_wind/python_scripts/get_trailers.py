import csv
import time
from duckduckgo_search import DDGS  # DuckDuckGo Search library
from googlesearch import search  # Google Search library

# Input and output file paths
input_csv = "/home/st0ic/Desktop/MIMUW/Informatics/IV_semester/WWW/P4/jekyll_tail_wind/_data/movies_data.csv"
output_csv = "/home/st0ic/Desktop/MIMUW/Informatics/IV_semester/WWW/P4/jekyll_tail_wind/_data/movies_data2.csv"

def count_csv_rows(file_path):
    with open(file_path, "r") as file:
        reader = csv.reader(file)
        row_count = sum(1 for row in reader)  # Count all rows
    return row_count - 1  # Subtract 1 for the header row

# Function to search for trailers using DuckDuckGo
def get_trailer_link_duckduckgo(movie_title):
    search_query = f"{movie_title} trailer site:youtube.com"
    try:
        with DDGS() as ddgs:
            results = ddgs.text(search_query)
            print(results)
            for result in results:
                if "youtube.com" in result["href"]:
                    return result["href"]  # Return the first YouTube link
    except Exception as e:
        print(f"DuckDuckGo rate-limited for {movie_title}: {e}")
    return None

# Function to search for trailers using Google Search
def get_trailer_link_google(movie_title):
    search_query = f"{movie_title} trailer site:youtube.com"
    try:
        for result in search(search_query, num_results=10):
            if "youtube.com" in result:
                return result  # Return the first YouTube link
    except Exception as e:
        print(f"Error fetching trailer from Google for {movie_title}: {e}")
    return "Trailer not found"

# Function to get trailer link with fallback
def get_trailer_link(movie_title):
    print(f"Searching DuckDuckGo for: {movie_title}")
    trailer_link = get_trailer_link_duckduckgo(movie_title)
    if trailer_link:
        return trailer_link
    
    print(f"DuckDuckGo rate-limited or no results. Falling back to Google for: {movie_title}")
    return get_trailer_link_google(movie_title)

# Read the input CSV and fetch trailer links
existing = count_csv_rows(output_csv)
with open(input_csv, "r") as infile, open(output_csv, "a", newline="") as outfile:
    reader = csv.DictReader(infile)
    fieldnames = reader.fieldnames + ["trailer"]
    writer = csv.DictWriter(outfile, fieldnames=fieldnames)
    
    # writer.writeheader()
    for index, row in enumerate(reader):
        print(index)
        if index < existing:
            continue
        # Extract the movie title after ") "
        movie_title = row["title"].split(") ", 1)[-1]
        count = 0
        while True:
            print(f"Fetching trailer for: {movie_title}")
            trailer_url = get_trailer_link(movie_title)
            if trailer_url == "Trailer not found":
                count += 1
                print(count)
                time.sleep(300 * count)
            else:
                row["trailer"] = trailer_url
                break
            
        writer.writerow(row)
        time.sleep(40)  # Delay to avoid overwhelming the search engines

print("Trailer fetching completed. Results saved to:", output_csv)
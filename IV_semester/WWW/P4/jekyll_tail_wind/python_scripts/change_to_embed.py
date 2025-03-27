import pandas as pd

# Input and output file paths
input_csv = "/home/st0ic/Desktop/MIMUW/Informatics/IV_semester/WWW/P4/jekyll_tail_wind/_data/movies_data2.csv"
output_csv = "/home/st0ic/Desktop/MIMUW/Informatics/IV_semester/WWW/P4/jekyll_tail_wind/_data/movies_data2_embed.csv"

def convert_to_embed(youtube_url):
    """
    Converts a standard YouTube URL to an embed URL.
    Example: https://www.youtube.com/watch?v=Dea4bypvgao -> https://www.youtube.com/embed/Dea4bypvgao
    """
    if "youtube.com/watch?v=" in youtube_url:
        video_id = youtube_url.split("watch?v=")[-1]
        return f"https://www.youtube.com/embed/{video_id}"
    elif "youtu.be/" in youtube_url:
        video_id = youtube_url.split("youtu.be/")[-1]
        return f"https://www.youtube.com/embed/{video_id}"
    return youtube_url  # Return the original URL if it's not a YouTube link

# Read the input CSV into a pandas DataFrame
df = pd.read_csv(input_csv)
print(df)
# Convert the YouTube links in the "trailer" column to embed URLs
if "trailer" in df.columns:
    df["trailer"] = df["trailer"].apply(convert_to_embed)

# # Save the updated DataFrame to a new CSV file
df.to_csv(output_csv, index=False)

print(f"Updated YouTube links saved to: {output_csv}")
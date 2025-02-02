import pandas as pd

# Function to generate SQL insert commands from a CSV
def generate_sql_insert(csv_file_path, output_file, table_name):
    # Read the CSV file into a DataFrame
    df = pd.read_csv(csv_file_path)
    
    # Open the output file for writing
    with open(output_file, 'w', encoding='utf-8') as file:
        # Iterate through the rows in the DataFrame
        for index, row in df.iterrows():
            # Get the column names and values
            columns = ', '.join(df.columns)
            
            # Escape any single quotes and ampersands in the values
            values = []
            for value in row:
                if isinstance(value, str):
                    # Escape single quotes by replacing them with two single quotes
                    escaped_value = value.replace("'", "''")
                    # Optionally, escape ampersands (if necessary, for systems where & is problematic)
                    escaped_value = escaped_value.replace("&", "'||'&'||'")
                    escaped_value = escaped_value.replace("ń", "n").replace("ą", "a").replace("ę", "e").replace("ć", "c").replace("Ą", "A").replace("Ń", "N").replace("Ę", "E").replace("Ó", "O").replace("ó", "o").replace("ł", "l").replace("Ł", "L").replace("ś", "s").replace("Ś", "S").replace("ź", "z").replace("Ź", "Z").replace("ż", "z").replace("Ż", "Z")

                    values.append(f"'{escaped_value}'")
                else:
                    values.append(f"{value}")

            # Join the values as a comma-separated string
            values_str = ', '.join(values)

            # Create the SQL insert command
            insert_command = f"INSERT INTO {table_name} ({columns}) VALUES ({values_str});"
            
            # Write the SQL command to the output file
            file.write(insert_command + '\n')

# Main script execution
if __name__ == "__main__":
    # Path to your CSV file
	print("Input path to file: ")
	csv_file_path = input()

    # Path to the output SQL fil

	print("Input output file: ")
	output_file = input() 

    # Specify the table name where data will be inserted
	print("Table name: ")
	table_name = input()# Change this to your table name

    # Generate SQL insert commands from the CSV and write to a file
	generate_sql_insert(csv_file_path, output_file, table_name)

	print(f"SQL insert statements written to {output_file}")

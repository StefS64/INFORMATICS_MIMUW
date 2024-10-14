#include <iostream>
#include <unordered_map>
#include <regex>
#include <array>
#include <vector>
#include <algorithm>

using namespace std;

#define NUMBER_OF_MEDALS 3

using Medals = array<size_t, NUMBER_OF_MEDALS>;
using MedalRanking = unordered_map<string, Medals>;
using ScoreOfCountry = pair<size_t, string>;
using CountryScores = vector<ScoreOfCountry>;

/** Compare that sorts from smallest to largest score of country
    and reversed lexicographical order, 
    because the ranking will be printed from the end of the vector.
 */
static bool compare(const ScoreOfCountry& a, const ScoreOfCountry& b) {
    if (a.first != b.first) {
        return a.first < b.first; 
    }
    return a.second > b.second;
}

static void print_error(size_t line_number) {
    cerr << "ERROR " << line_number << '\n';
}

int main() {
    /** Three regex expressions which parse input into:
        1. Country name and medal number from 1 to 3.
        2. Country name and medal number from 0 to 3.
        3. 3 numbers determining the weights odf the medals.
    */
    regex revoke_medal(R"(-([A-Z][A-Za-z ]*[A-Za-z]) ([1-3]))");
    regex award_medal(R"(([A-Z][A-Za-z ]*[A-Za-z]) ([0-3]))");
    regex standings(R"(=([1-9]\d{0,5}) ([1-9]\d{0,5}) ([1-9]\d{0,5}))");

    size_t line_num = 1;
    string line;
    MedalRanking medal_ranking;

    while (getline(cin, line)) {
        smatch elements;
        string country;
        size_t medal;
        // Check which regex works and extract the values to elements.
        if (regex_match(line, elements, award_medal)) {
            country =  elements[1];
            medal = stoul(elements[2]);                
            auto iterator = medal_ranking.find(country);

            // Add country if it didn't exist.
            if (iterator == medal_ranking.end()){
                Medals initialize_medals = { 0 };
                iterator = medal_ranking.emplace(
                    country,
                    initialize_medals
                ).first;
            }

            /** Award appropriate medal, the country always exists
                because last if statement assures that.
            */
            if (medal != 0){
                Medals& current_country_medals = iterator->second;
                current_country_medals[medal - 1]++;
            }
        } else if (regex_match(line, elements, revoke_medal)) {
            country =  elements[1];
            medal = stoul(elements[2]) - 1;
            auto iterator = medal_ranking.find(country);
            
            // Remove medal, print ERROR if medal doesn't exist.
            if (iterator != medal_ranking.end()) { 
                Medals& current_country_medals = iterator->second;
                
                if (current_country_medals[medal] == 0) {
                    print_error(line_num);
                } else {
                    current_country_medals[medal]--;
                }
            } else {
                print_error(line_num);
            }
        
        } else if (regex_match(line, elements, standings)) {                
            CountryScores current_standing;
            Medals medal_weights;

            // Convert to numbers from string, 
            for (size_t i = 0; i < NUMBER_OF_MEDALS; i++) {
                medal_weights[i] = stoul(elements[i + 1]);
            }

            // Calculation of country score.
            for (const auto& [country,  medals] : medal_ranking) {
                size_t score =  0;
                
                for (size_t i = 0; i < NUMBER_OF_MEDALS; i++){
                    score += medal_weights[i] * medals[i];
                }

                current_standing.push_back({score, country}); 
            }

            /** There is no need for a comparator, 
                because score is the first element in pair and
                the country name is the second.
            */
            sort(current_standing.begin(), current_standing.end(), compare);
            
            size_t rank = 1;
            size_t position = 1;
            size_t last_score = 0;

            /** Print the sorted ranking if scores are equal they, 
                get the same postion. 
                Going through the vector and yanking best country.
            */
            while (!current_standing.empty()) {
                ScoreOfCountry top = current_standing.back();

                if (last_score != top.first) {
                    rank = position;
                    last_score = top.first;
                }

                cout << rank << ". " << top.second << '\n';

                position++;
                current_standing.pop_back();
            }                
        } else {
            print_error(line_num);
        }
    line_num++;
    }
}
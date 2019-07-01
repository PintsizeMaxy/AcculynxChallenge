# AcculynxChallenge
Pulls data using the StackExchange API and displays each questions where is_accepted == true and the num_of_answers > 1.
Pulls 100 questions dated within a one week time frame and adds them to a list of questions IF the question has more than 1 answer
belonging to it as well as having an accepted answer. Allows users to click questions to get the list of answers belonging to the
question. User will then be able to select which answer they think is correct, after guessing the correct answer, the user will have
the total amount of votes the answer received added to their score. Each incorrect guess will subtract the wrong answer's vote from
user's current score. User will see their new updated score after guessing the correct answer. Search bar at the top will actively load
in any question that contains the substring in the title and update their positions to correspond to the new location in the recyclerview
that the question is now located at. After selecting correct answer, user can view the votes the correct answer was worth.

SELECT
Hackers.hacker_id, Hackers.name
FROM
Submissions
INNER JOIN Hackers ON Hackers.hacker_id = Submissions.hacker_id
INNER JOIN Challenges ON Challenges.challenge_id = Submissions.challenge_id
INNER JOIN Difficulty ON Difficulty.difficulty_level = Challenges.difficulty_level
WHERE
Submissions.score = Difficulty.Score AND Difficulty.difficulty_level = Challenges.difficulty_level
GROUP BY Hackers.hacker_id, Hackers.name
HAVING COUNT(*) > 1
ORDER BY COUNT(*) DESC, Hackers.hacker_id ASC
SELECT
Hackers.hacker_id, name, SUM(MaxScore) AS TotalScore
FROM (
    SELECT hacker_id, challenge_id, MAX(score) AS MaxScore
    FROM Submissions
    GROUP BY hacker_id, challenge_id
) AS tmp1
INNER JOIN Hackers ON Hackers.hacker_id = tmp1.hacker_id
GROUP BY hacker_id, name
HAVING TotalScore <> 0
ORDER BY TotalScore DESC, hacker_id ASC
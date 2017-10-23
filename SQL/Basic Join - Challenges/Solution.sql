SELECT
Hackers.hacker_id, 
Hackers.name, 
COUNT(*) AS ChalCount
FROM
Hackers
INNER JOIN Challenges ON Challenges.hacker_id  = Hackers.hacker_id
GROUP BY hacker_id, name
HAVING 
ChalCount = (
    SELECT MAX(tmp1.Cnt) FROM (SELECT COUNT(*) AS Cnt FROM Challenges GROUP BY hacker_ID) AS tmp1
) 
OR
ChalCount IN (
    SELECT tmp2.Cnt FROM (
		SELECT COUNT(*) AS Cnt FROM Challenges GROUP BY hacker_ID
	) AS tmp2
	GROUP BY tmp2.Cnt
	HAVING COUNT(tmp2.Cnt) = 1
)
ORDER BY ChalCount DESC, hacker_id
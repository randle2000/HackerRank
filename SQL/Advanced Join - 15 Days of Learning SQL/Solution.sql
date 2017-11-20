SELECT 
submission_date,

(
	SELECT COUNT(DISTINCT hacker_id)
	FROM Submissions AS tmp2
	WHERE 
		tmp2.submission_date = tmp1.submission_date AND
		(
			SELECT COUNT(DISTINCT submission_date) FROM Submissions AS tmp3 WHERE tmp3.hacker_id = tmp2.hacker_id AND tmp3.submission_date < tmp2.submission_date
		) = -DATEDIFF('2016-03-01', tmp1.submission_date)
		
) AS Cnt,


(
	SELECT hacker_id
	FROM Submissions AS tmp2
	WHERE tmp2.submission_date = tmp1.submission_date
	GROUP BY hacker_id
	ORDER BY COUNT(submission_id) DESC, hacker_id
	LIMIT 1
) AS h_id,
(SELECT name FROM hackers WHERE hackers.hacker_id = h_id)

FROM
(SELECT DISTINCT submission_date FROM Submissions) AS tmp1
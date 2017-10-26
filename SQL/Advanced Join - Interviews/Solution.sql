SELECT
Contests.contest_id, hacker_id, name, 
IFNULL(SUM(sum_ts), 0) AS ts, IFNULL(SUM(sum_tas), 0) AS tas,
IFNULL(SUM(sum_tv), 0) AS tv, IFNULL(SUM(sum_uv), 0) AS uv
FROM
Contests
INNER JOIN Colleges ON Colleges.contest_id = Contests.contest_id
INNER JOIN Challenges ON Challenges.college_id = Colleges.college_id

LEFT JOIN (
    SELECT challenge_id, SUM(total_views) AS sum_tv,  SUM(total_unique_views) AS sum_uv 
    FROM View_Stats 
    GROUP BY challenge_id
) AS tmp1 ON tmp1.challenge_id = Challenges.challenge_id

LEFT JOIN (
    SELECT challenge_id, SUM(total_submissions) AS sum_ts, SUM(total_accepted_submissions) AS sum_tas 
    FROM Submission_Stats 
    GROUP BY challenge_id
) AS tmp2 ON tmp2.challenge_id = Challenges.challenge_id

GROUP BY contest_id, hacker_id, name
HAVING NOT (ts =0  AND tas = 0 AND tv = 0 AND uv = 0)
ORDER BY contest_id
SELECT
Start_Date, MIN(End_Date)
FROM (
    SELECT Start_Date FROM Projects WHERE Start_Date NOT IN (SELECT End_Date FROM Projects)
) AS tmp1
INNER JOIN (
    SELECT End_Date FROM Projects WHERE End_Date NOT IN (SELECT Start_Date FROM Projects)
) AS tmp2
WHERE Start_Date < End_Date
GROUP BY Start_Date
ORDER BY DATEDIFF(MIN(End_Date), Start_Date), Start_Date
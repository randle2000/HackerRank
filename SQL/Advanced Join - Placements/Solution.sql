SELECT
name
FROM
Students
INNER JOIN Friends ON Friends.ID = Students.ID
INNER JOIN Packages AS OwnSal ON OwnSal.ID = Students.ID
INNER JOIN Packages AS FriSal ON FriSal.ID = Friends.Friend_ID
WHERE FriSal.Salary > OwnSal.Salary
ORDER BY FriSal.Salary
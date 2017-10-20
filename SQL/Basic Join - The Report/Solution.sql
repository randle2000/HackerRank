SELECT
IF(Grade >= 8, Name, NULL) AS Nm,
Grade,
Marks
FROM 
Students
INNER JOIN Grades ON Grades.Min_Mark <= Students.Marks AND Grades.Max_Mark >= Students.Marks
ORDER BY Grade DESC, Nm ASC, Marks ASC
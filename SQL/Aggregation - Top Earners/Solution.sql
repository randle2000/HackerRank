SELECT
salary * months AS earnings,
COUNT(*)
FROM
Employee
GROUP BY earnings
HAVING earnings = (SELECT MAX(salary * months) FROM Employee)
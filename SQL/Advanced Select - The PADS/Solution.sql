(SELECT
CONCAT(Name, '(', LEFT(Occupation, 1), ')') AS Nm
FROM OCCUPATIONS)

UNION

(SELECT CONCAT('There are a total of ', tmp.Cnt, ' ', tmp.Occup, 's.')
FROM
(SELECT COUNT(Occupation) AS Cnt, LOWER(Occupation) AS Occup FROM OCCUPATIONS GROUP BY Occup ORDER BY Cnt, Occup) AS tmp)

ORDER BY Nm
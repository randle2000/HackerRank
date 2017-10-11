SELECT 
    MIN(CASE Occupation WHEN 'Doctor' THEN Name ELSE NULL END) AS Doctor,
    MIN(CASE Occupation WHEN 'Professor' THEN Name ELSE NULL END) AS Professor,
    MIN(CASE Occupation WHEN 'Singer' THEN Name ELSE NULL END) AS Singer,
    MIN(CASE Occupation WHEN 'Actor' THEN Name ELSE NULL END) AS Actor
FROM
    (SELECT
    Name, Occupation,
    (SELECT COUNT(*) FROM OCCUPATIONS AS tmp1 WHERE tmp1.Occupation = tmp2.Occupation AND tmp2.Name > tmp1.Name) AS Rank
    FROM OCCUPATIONS AS tmp2) AS tmp3
GROUP BY
    Rank
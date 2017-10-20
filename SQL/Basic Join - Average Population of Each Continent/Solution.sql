SELECT
COUNTRY.Continent,
FLOOR(AVG(CITY.Population))
FROM 
COUNTRY
INNER JOIN CITY ON COUNTRY.CODE = CITY.COUNTRYCODE
GROUP BY Continent
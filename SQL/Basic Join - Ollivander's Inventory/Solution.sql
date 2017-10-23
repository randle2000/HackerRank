SELECT 
id, age, MinCoins, Wands.power
FROM
Wands
INNER JOIN (
    SELECT
    Wands.code,
    age,
    MIN(coins_needed) AS MinCoins,
    power
    FROM 
    Wands
    INNER JOIN Wands_Property ON Wands_Property.code = Wands.code
    WHERE NOT is_evil
    GROUP BY power, age, code
) AS tmp1 ON tmp1.code = Wands.code AND tmp1.MinCoins = Wands.coins_needed
ORDER BY power DESC, age DESC
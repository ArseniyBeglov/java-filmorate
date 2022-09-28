# java-filmorate
Template repository for Filmorate project.
[link for diagram](https://app.quickdatabasediagrams.com/#/d/UfZYQy)
![This is an diagram](https://github.com/ArseniyBeglov/java-filmorate/blob/main/QuickDBD-Diagram.png?raw=true)

Примеры запроса:
для User мы помем получить таблицу с фильмами которые он лайкнул, а также таблицу с его друзьями
для Film таблицу с его жанрами или рейтинг, кол-во лайков
нв sql:
SELECT t.film_id,
        COUNT(t.user_id)
FROM FilmLikes AS T
GROUP BY t.film_id;


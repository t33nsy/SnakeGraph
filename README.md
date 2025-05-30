Граф-змея - полугамильтонов граф (существует гамильтонов путь), являющийся подграфом n-мерного гиперкуба. Другими словами, отображая на плоскость, граф-змея - цепь с дополнительными диагоналями (ребрами между несмежными вершинами).

Алгоритм состоит из двух частей:
1. Проверка наличия гамильтонова пути - бэктрекинг с проверкой возможности обхода в глубину по всем вершинам по одному разу
2. Проверка вкладываемости в n-куб (является ли граф подграфом n-мерного гиперкуба), используются следующие рассуждения:
   - Граф связный (проверено ранее, так как производился поиск гамильтонова пути)
   - Граф состоит из 2^n вершин, где n - какое-то целое число (подбираем n в цикле)
   - Ребра графа соединяют только те вершины, расстояние Хэмминга между которыми равно 1 (бинарные метки отличаются в 1 бите)
   - Следовательно, применяем алгоритм перебора (бэктрекингом) бинарных векторов длины n, пытаясь присвоить их каждой вершине и проверить расстояния Хэмминга
3. Итоговый результат - логическое И между результатом первой и второй проверки

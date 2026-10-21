matriz :: [[Int]]
matriz = [[1, 2, 3, 4, 5],[6, 7, 8, 9, 10],[11, 12, 13, 14, 15]]

transposta :: [[a]] -> [[a]]
transposta [] = []
transposta ([]:_) = [] --se a primeira linha for vazia, e provavelmente as outras são tbm
transposta m = [x | (x:_) <- m] : transposta [xs | (_:xs) <- m]
--Em haskell uma matriz ~e uma linha dai fazendo o head dos elementos dela vai ser o head
--das listas dela dai puxo recursivamente pro tail

diagonal :: [[Int]] -> [Int]
diagonal [] = []
diagonal ([]:_) = []
diagonal ((x:_):linhas) = x : diagonal [xs | (_:xs) <- linhas]

soma :: [Int] -> Int
soma [] = 0
soma (x:xs) = x + soma xs

main :: IO()
main = 
    do
    print(map(\x -> x*x) [1..4])
    print(transposta matriz)
    print(diagonal matriz)
    print(soma [1, 2, 3, 4])
funcaoQualquer :: String -> IO()
funcaoQualquer x = putStrLn x

ehVdd :: Bool -> IO()
ehVdd x = if x == True 
          then putStrLn "É vdd"
          else return ()

ehVdComGuard :: Bool -> IO()
ehVdComGuard x 
            | x = putStrLn "É vdd com guard"
            | otherwise = return ()

classificaIdade :: Int -> String
classificaIdade idade
    | idade < 0   = "Idade inválida!"
    | idade < 12  = "Criança"
    | idade < 18  = "Adolescente"
    | idade < 60  = "Adulto"
    | otherwise   = "Idoso"
--guards funcionam como um else if

retornaQuadrado :: Float -> Float
retornaQuadrado x = x*x

soma :: Int -> Int -> Int
soma a b = a+b

nome :: String
nome = "Ada"

tuplaNomeEIdade :: (String, Int)
tuplaNomeEIdade = ("Ada", 36)

retornaAreaCirculo :: Double -> Double
retornaAreaCirculo r = pi*r*r

conceito :: Int -> Char
conceito n 
    | n <= 50 = 'D'
    | n <= 70 = 'C'
    | n <= 90 = 'B'
    | otherwise = 'A'

tamanho :: [Int] -> Int
tamanho [] = 0
tamanho (x:xs) = 1 + tamanho xs

maxi :: [Int] -> Int
maxi [] = 0
maxi [x] = x
maxi (x:xs)
    | x > maiorResto = x
    | otherwise = maiorResto
    where maiorResto = maxi xs

maiorLista :: [Int] -> Int
maiorLista [x] = x
maiorLista (x:xs) = max x (maiorLista xs)

somaQuadrados :: [Int] -> Int
somaQuadrados = sum . map (^2)

somaImapares :: [Int] -> Int
somaImapares = sum . filter (\x -> x `mod` 2 == 1)

data Forma
    = Circulo Double
    | Retangulo Double Double
    | Triangulo Double Double
    deriving Show

area :: Forma -> Double
area (Circulo r) = pi*r*r
area (Retangulo l a) = l*a
area (Triangulo l a) = l*a/2

premeiro :: [a] -> Maybe a
premeiro [] = Nothing
premeiro (x:_) = Just x

main :: IO()
main = do
    funcaoQualquer ("Eita pox"++"a\n") 
    funcaoQualquer "True"
    ehVdd True
    ehVdComGuard True
    putStrLn (classificaIdade 34)
    print (retornaQuadrado 24)
    print (nome)
    print(soma 2 3)
    print(retornaAreaCirculo (-1))
    print(conceito 90)
    print(tamanho [1, 2, 3])
    print(maxi [2, 3, 4])
    print(maiorLista [2, 3, 4, 5, 6])
    print(somaImapares [2, 4, 5])
    print(area(Triangulo 5 2))
    print(area(Retangulo 5 2))
    print(premeiro [3, 4] :: Maybe Int)

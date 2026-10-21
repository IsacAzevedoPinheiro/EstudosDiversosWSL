main :: IO()
main = do
    let ola = "Olá mundo"
    let x = getLine
    putStrLn ola
    putStrLn "Entre com seu nome: "
    ola <- x
    putStrLn ("Olá " ++ ola)
    --O Haskell criou outro ola

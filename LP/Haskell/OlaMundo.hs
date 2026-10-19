main :: IO()
main = do
    let ola = "Olá mundo"
    putStrLn ola
    putStrLn "Entre com seu nome: "
    ola <- getLine
    putStrLn ("Olá " ++ ola)
    --O Haskell criou outro ola

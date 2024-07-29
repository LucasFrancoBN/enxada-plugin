# Plugin de minecraft - Desafio EnxadaHost

## Como rodar o projeto
Para rodar o projeto, crie um server spigot localmente, então pegue o arquivo "EnxadaPlugin-1.0-SNAPSHOT.jar" e cole no diretório de plugin do servidor.
Agora vamos criar o banco de dados, nesse caso, utilizei o MySQL na sua versão 8. Agora, rode os comandos:

Detalhe: o seu banco de dados precisa estar de acordo com o arquivo `config.yml`

```mysql
CREATE TABLE tb_home (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cooldown INT,
    apelido_home VARCHAR(255),
    particulas BOOLEAN,
    x DOUBLE,
    y DOUBLE,
    z DOUBLE
);

CREATE TABLE tb_player (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(255) NOT NULL
);

CREATE TABLE tb_player_home (
    player_id INT NOT NULL,
    home_id INT NOT NULL,
    PRIMARY KEY (player_id, home_id),
    FOREIGN KEY (player_id) REFERENCES tb_player(id),
    FOREIGN KEY (home_id) REFERENCES tb_home(id)
);

CREATE TABLE tb_cooldown (
    player INT NOT NULL,
    ultima_data DATETIME NOT NULL,
    PRIMARY KEY (player)
);
```

Agora é só rodar o projeto em sua máquina :D

## Vídeo de demonstração:

Testando windcharge: [Assista ao vídeo](https://youtu.be/LB1f9hr6mZk) 

OBS: infelizmente não teve nenhum mob para mostrar a força do windcharge, mas está configurado.

Testando home: [Assista ao vídeo](https://youtu.be/XZnqYSpvIAA)


# Executando o projeto localmente

Este é um guia para rodar o projeto localmente com a imagem do ServeRest e executar a suíte diretamente.

Checklist rápido
- Ter Java 17, Maven e Docker instalados.
- Clonar o repositório.
- Subir a imagem do ServeRest em `localhost:3000`.
- Abrir o projeto no IDE (IntelliJ) e executar a classe runner (ou executar via Maven se preferir).

Passos:

1) Clonar o repositório e entrar na pasta:

```bash
git clone https://github.com/Marina-Gajego/serverest-bmad-tea-automation-qa.git
cd serverest-bmad-tea-automation-qa
```

2) Subir a API ServeRest via Docker (comando que faz pull automático):

```bash
docker run -d --name serverest -p 3000:3000 paulogoncalvesbh/serverest:latest
```

3) Abrir o projeto no IntelliJ (ou outro IDE Java) e executar a classe runner do Cucumber:

- Classe runner: `br.com.marina.qa.runner.RunnerTest` (localize em `src/test/java/.../runner` ou execute a classe `RunnerTest` no IDE clicando em Run).

Observação: executar a classe runner no IDE é o jeito mais direto — não é necessário comandos adicionais além de subir a imagem.

Alternativa rápida via Maven (opcional):

```bash
# Executa a classe runner via Maven (se preferir linha de comando)
mvn -Dtest=br.com.marina.qa.runner.RunnerTest test
```

Parar/remover o container quando terminar:

```bash
docker stop serverest && docker rm serverest
```

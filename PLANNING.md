# Planejamento Estratégico: App Copa 2026

Este documento descreve o plano de evolução do aplicativo "Copa 2022" para uma versão aprimorada e focada na Copa do Mundo de 2026, com o objetivo de criar uma peça de destaque para portfólio.

---

## Fase Zero: Análise de Riscos e Mitigação

Análise proativa dos desafios técnicos e estratégias para superá-los.

| Risco Potencial | Impacto | Estratégia de Prevenção e Mitigação |
| :--- | :--- | :--- |
| **1. Fonte de Dados (API):** Acesso a dados dinâmicos e estáticos para o app. | **Crítico** | **Estratégia Híbrida:** Para dados estáticos (times, jogadores, calendário), criaremos nossa própria API via JSON hospedado no GitHub (controle total, custo zero). Para dados dinâmicos (placar ao vivo, bolão), utilizaremos um serviço como o Firebase. |
| **2. Backend para Funções Sociais:** Recursos como "Bolão" e "Comentários" exigem um servidor. | **Alto** | **Abordagem Faseada:** Utilizar um "Backend as a Service" (BaaS) como o **Firebase (Firestore, Authentication)** para simplificar a infraestrutura. |
| **3. Complexidade da UI/UX:** Gerenciar telas complexas (mapas, placares ao vivo) em Compose. | **Médio** | **Design de Componentes:** Prototipar a UI e quebrá-la em pequenos componentes reutilizáveis e fáceis de testar. |
| **4. Mudança no Formato da Copa:** O novo formato com 48 times tornará os modelos de dados atuais obsoletos. | **Alto** | **Modelos de Dados Flexíveis:** Desenhar `data class` pensando em flexibilidade, evitando números "hardcoded" e derivando a estrutura da resposta da API. |
| **5. Gerenciamento de Fuso Horário:** Lidar com jogos em 3 países diferentes (EUA, Canadá, México). | **Médio** | **Padrão de Data/Hora:** Armazenar todas as datas no padrão **UTC**. Converter para o fuso local do dispositivo apenas no momento da exibição. |

---

## O Roteiro Priorizado de Funcionalidades

Uma ordem de implementação lógica que constrói valor de forma incremental.

**Prioridade 1: A Fundação para 2026**
- **Feature:** Adaptação do Núcleo para a Copa 2026.
- **Ações:** Criar a v1 da API estática (JSON), redefinir os modelos de dados no app, e atualizar a UI para o novo nome "Copa 2026".

**Prioridade 2: Engajamento Essencial**
- **Feature:** Escolha de "Seleção Favorita".
- **Ações:** Criar tela de boas-vindas, salvar a escolha localmente (DataStore), destacar jogos do time favorito.

**Prioridade 3: Conteúdo Principal**
- **Feature:** Tela de Detalhes da Partida e Tabela de Classificação.
- **Ações:** Criar tela de detalhes, implementar a lógica e a UI para as tabelas de classificação dos grupos.

**Prioridade 4: O Diferencial Geográfico**
- **Feature:** Guia das Sedes com Mapa Interativo.
- **Ações:** Integrar o SDK do Google Maps para exibir os estádios em um mapa interativo.

**Prioridade 5: O Recurso de Mestre (O "Bolão")**
- **Feature:** Bolão Simples (Fase 1).
- **Ações:** Configurar Firebase (Auth, Firestore), criar telas de login/cadastro e lógica de palpites.

**Prioridade 6: Polimento e Conteúdo Extra**
- **Feature:** Feed de Notícias e Guia de Seleções.
- **Ações:** Integrar um leitor de RSS e criar as telas com informações detalhadas sobre as seleções.

**Prioridade 7: Features de Vanguarda**
- **Feature:** Placar ao Vivo e Comentários.
- **Ações:** Utilizar Firebase Realtime Database ou Firestore Listeners para atualizações em tempo real.

---

## Primeiro Passo Concreto

**Ação Imediata:** Projetar e criar a v1 da nossa **API estática (JSON)** com os dados base da Copa 2026 (ex: lista inicial de times, formato dos grupos, etc.).


.Acknowledge the NetworkModule.kt update.2.Suggest updating the README.md file with the new API URL.3.Update the README.md file.4.Declare final victory and ask the user to run the app.
# Plano de Evolução do App Copa 2022 -> 2026

Este documento estrutura o desenvolvimento de novas funcionalidades, priorizando o caminho de menor resistência e maior impacto para transformar o app em um ecossistema completo para o torcedor.

---

### Fase 1: Fundação do Engajamento (Curto Prazo)

*Prioridade: Alta. Foco em personalização e na funcionalidade chave de retenção.*

1.  **Onboarding e Personalização:**
    *   **Tarefa:** Criar uma tela única de boas-vindas após a Splash Screen.
    *   **Detalhes:** Perguntar ao usuário "Qual sua seleção favorita?" e "Deseja ativar notificações?".
    *   **Lógica:** Esta é a base para a personalização da Home e das notificações. É um pré-requisito para quase tudo que virá depois.

2.  **Home Dinâmica (MVP):**
    *   **Tarefa:** Modificar a tela principal para exibir um card fixo com o próximo jogo do time favorito do usuário.
    *   **Lógica:** Entrega valor imediato da personalização feita no Onboarding. Baixo esforço, alto impacto percebido.

3.  **Estrutura do Bolão (Simples):**
    *   **Tarefa:** Criar a tela do bolão onde o usuário pode dar palpites nos jogos.
    *   **Detalhes:** Apenas palpites individuais, sem sistema de pontos complexo ou ligas por enquanto. Focar na UI de inserir o placar.
    *   **Lógica:** Introduz a principal mecânica de gamificação de forma simples, validando o interesse do usuário antes de construir o sistema completo.

---

### Fase 2: Gamificação e Conteúdo (Médio Prazo)

*Prioridade: Média. Foco em aprofundar a retenção e enriquecer a experiência.*

1.  **Bolão Avançado - Ligas e Ranking:**
    *   **Tarefa:** Implementar a lógica de pontuação, um ranking global e a funcionalidade de criar/participar de ligas privadas.
    *   **Detalhes:** Sistema de pontos (acertar placar, vencedor, etc.) e links de convite para ligas.
    *   **Lógica:** Transforma o bolão em uma ferramenta social e competitiva, o principal motor de engajamento diário.

2.  **Notificações Interativas:**
    *   **Tarefa:** Expandir o `NotificationMatcherWorker` existente.
    *   **Detalhes:** Adicionar notificações para gols, início/fim de jogo e cartões para os times que o usuário favoritou.
    *   **Lógica:** Aumenta os pontos de contato com o usuário de forma relevante, trazendo-o de volta ao app nos momentos mais importantes.

3.  **Feed de Notícias (Agregador):**
    *   **Tarefa:** Criar uma seção/carrossel na Home que exibe notícias de fontes externas.
    *   **Detalhes:** Usar um feed RSS de um portal de esportes conhecido. Não é preciso criar conteúdo próprio.
    *   **Lógica:** Mantém o app atualizado e relevante com baixo esforço de manutenção.

---

### Fase 3: Ecossistema Completo (Longo Prazo)

*Prioridade: Baixa. Foco em funcionalidades de "luxo" que solidificam a comunidade.*

1.  **"Modo Estádio" e Guia de Sedes:**
    *   **Tarefa:** Implementar a conversão automática de fuso horário e criar cards com informações sobre os estádios e cidades-sede.
    *   **Lógica:** Funcionalidade de alta utilidade que diferencia o app, especialmente útil para a Copa de 2026.

2.  **Ficha Técnica Completa:**
    *   **Tarefa:** Ao clicar em uma partida, exibir mais detalhes como histórico de confrontos (H2H), escalações confirmadas e curiosidades.
    *   **Lógica:** Aprofunda o conteúdo para os usuários mais "hardcore".

3.  **Conteúdo Gerado pelo Usuário (UGC):**
    *   **Tarefa:** Criar uma galeria "Minha Torcida" onde usuários podem postar fotos.
    *   **Lógica:** O passo final para criar um sentimento de comunidade, onde o usuário não é apenas um consumidor, mas um participante ativo.


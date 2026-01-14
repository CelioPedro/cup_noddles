# Planejamento de Refatoração da MainViewModel

## Fase 1: Refatoração da Arquitetura (Prioridade Alta)

**Objetivo:** Alinhar a `MainViewModel` com os princípios da Arquitetura Limpa, removendo dependências diretas de repositórios e utilizando Casos de Uso (UseCases) para todas as interações com a camada de dados.

### Passo 1.1: Criar Casos de Uso para Sincronização

- [ ] Criar o arquivo `SyncMatchesUseCase.kt` no pacote `domain/usecase`.
- [ ] Criar o arquivo `SyncTeamsUseCase.kt` no pacote `domain/usecase`.

### Passo 1.2: Refatorar a MainViewModel

- [ ] Remover a injeção de `MatchesRepository` e `TeamsRepository` da `MainViewModel`.
- [ ] Injetar `SyncMatchesUseCase` e `SyncTeamsUseCase` na `MainViewModel`.
- [ ] Atualizar o método `fetchData` para utilizar os novos casos de uso em vez de chamar `repository.sync()` diretamente.

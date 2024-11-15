# ES-2024-1Sem-LETI-GrupoI

## Membros do Grupo
- Manuel Santos (111087) - [y0c0ms](https://github.com/y0c0ms)
- Miguel Reis (111035) - [miguelcreis](https://github.com/miguelcreis)
- João Oliveira (111568) - [JoaoOliveira](https://github.com/JoaoOIiveira)
- André Costa (11118) - [Andre3648](https://github.com/Andre3648)

### Principais Fuincionalidades
- **Adjacency Checker**: Verifica se duas propriedades são adjacentes pelos seus OBJECTIDs.
- **Property Map Visualizer**: Ver uma visualização em forma de mapa das propriedades e das suas relações de adjacência. (Ainda em muitos testes)

### Principais Problemas
- Issue 1: Inicialmente pensávamos que os grafos deveriam ser diagramas pelo que tentamos desenvolver uma classe que faria estes diagramas (sem muito sucesso).
- Issue 2: Gestão e documentação do código(será corrigido brevemente).
- Issue 3: O ficheiro CSV providenciado contem um erro de formatação no OBJECTID 9323 que teve que ser editada manualamnete. O programa na sua versão final deverá arranjar alguma forma de eleminar a linha retornando um aviso ou 
-Issue 4: A Gui apresentada é muito básica e serve apenas para a apresentação das funcionalidades inicias.

### Funcionalidades Incompletas

- Funcionalidade opcional: Implementar metodo que assume um objeto e passa a lista das suas propriedades adjacentes.
- Funcionalidade 1: Permita calcular a área média das propriedades, de uma área geográfica/administrativa indicada pelo utilizador (freguesia, concelho, distrito);
- Funcionalidade 2: Permita calcular a área média das propriedades, assumindo que propriedades adjacentes, do mesmo proprietário, devem ser consideradas como uma única propriedade, para uma área geográfica/administrativa indicada pelo utilizador;
- Funcionalidade 3: Permita representar sob a forma de um grafo (estrutura de dados) a relação de vizinhança entre proprietários, onde os nós representam os proprietários e as arestas representam as relações de vizinhança entre os nós (entre os proprietários). Considera-se que um proprietário é vizinho de outro proprietário se os mesmos possuírem propriedades contíguas;
- Funcionalidade 4: Permita gerar sugestões para troca de propriedades entre proprietários, que maximizem a área média das propriedades por proprietário (área média entendida segundo o cálculo descrito no ponto 4). Na sugestão de trocas, deve ser considerado não só o aumento de área média proporcionado por essa troca, mas também o potencial da troca ser realizada pelos proprietários. Assume-se que os proprietários envolvidos nas trocas têm interesse em incorrer nos custos mínimos possíveis para realizar a transação.

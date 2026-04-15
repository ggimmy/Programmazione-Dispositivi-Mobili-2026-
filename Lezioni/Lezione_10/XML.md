# PRIMA APP

## XML
Linguaggio di markup simile ad html, pensato però per strutturare ed organizzare informazioni.
I tag XML non sono predefiniti, pensato per essere un linguaggio autoesplicativo.
Al giorno d'oggi XML è stato rimpiazzato da JSON.
XML è pensato per creare strutture dati.
I documenti XML hanno una struttura ad albero, tutto ciò contenuto fra due tag è detto **elemento**.
`<root>`
` 	<child>`
`		<subchild> ... </subchild>`
`	<\child>`
` <\root>`

I tag vanno annidati bene, in piu il tag di chiusura è **obbligatorio**

Nel tag `<file>` dobbiamo specificare il tipo fra apici.
I valori fra apici sono detti **attributi**. Esempio `<note date="10/01/2008">` è un attributo

In XML un documento è **valido** se rispetta le regole formali. Posso però specificare la struttura di un documento con dei schemi XML o DTD. Se un documento rispetta le regole di un DTD o di uno schema, si dice
**corretto**

I namespace servono ad evitare il conflitto dei nomi, ossia definire schemi diversi per tag omonimi.

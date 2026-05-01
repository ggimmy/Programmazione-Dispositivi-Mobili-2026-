# Intent Impliciti

Non appiamo cosa o chi compierà l'azione ma chiediamo al sistema operativo.
Descrive un operazione da eseguire ed il S.O. cerca il componente più adatto.
La scelta dimende da **action** e **data**
Alcune azioni standard nelle classi intent sono:
1. `ACTION_VIEW`
2. `ACTION_DIAL`
3. `ACTION_SEND`

Ovviamente la scelta dipende dal tipo di dato (per esempio per un immagine faccio view)
Il dato può essere specificato tramite *MIME* (esempio `image/jpeg` per specifiare jpeg) o *URI*.

## Categorie
Servono per una sorta di tag negli intent filter come `CATEGORY_DEFAULT`, `CATEGORY_BROSABLE` etc...
Vanno nell'**intent filter** che contiene:
1. Azione
2. Categoria
3. Dato

I filtri possono essere in posti anche in URI e MIME

# TASKS
Le task sono l'insieme delle activity che compiono un azione. In condizione normale le activity aperte finiscono nello stesso task ed esse sono organizzate in uno stack detto **back stack**.

Una nuova activity viene aggiunta al task corrente, un nuovo task puo essere creato quando il comportamento predefinito viene modificato
I meccanismi principali di creazione task sono:
1. `launchMode` nel Manifest
2. flag aggiunti all'intent come `FLAG_ACTIVITY_NEW_TASK`

## Back Stack
L'activity in cima è uella visibile in foreground, quando l'utente torna indietro l'activity viene rimossa.
Quindi 1 sola activity puo essere in fooreground mentre le altre sono in background.
Una task in background conservano il propio back stack cosi che quando l'utente lo riapre puo riprendere dallo stato precedente.

# Risorse
La filosofia andorid è uella di esternalizzare le risorse per gestirle separatamente (immagini, stringhe, grafica, etc...) quindi si utilizzano risorse alternative per dispositivi diversi con configurazioni diversi.

Le **risorse** sono contenute nella cartella **res** che al suo interno contiene altre cartelle a seconda del tipo.
Solitamente ogni risorsa è pensata per un determinato tipo di dispositivo(esempio a seconda della lingua o della risoluzione), per questo ci sono ulteriori sottocartelle. GLi attributi di configurazioni si dicono `<config-qualifier>`. 
Esempio `drawable-en-rUS-land` ossia immagine per dispositivi inglesi regionali americani in landscape.
I qualificatori possono essere concatenati con un - .
Per conigurazione si intende: direzione testo, operatore SIM, lingua, dimensione e risoluzione dello schermo, etc...
La selezione della configurazione giusta avviene confrontando le risorse fornite con le caratteristiche del dispositivo

Le risorse si dividono in 2 tipi:
1. Valori (In questo caso la risorsa è identficata dall'attributo xml) 
2. File (In questo caso la risorsa è identificata dal nome del file)

## Classe R
La classe R è generata automaticamente in build e contiene gli identificaori delle risorse dell'app.(NB gli errori XML creano problemi perchè il compilatore non riesce a genereare R correttamente)
Ad ogni risorsa lui associa un numero a seconda del tipo e della risorsa.
Esempio:
``` kotlin

val title = stringResources(R.string.main_title)

```

## Dimension
RIsorse utilizzate per margini, padding, font size, larghezza e altezza di elementi UI

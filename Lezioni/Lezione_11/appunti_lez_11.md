# Commenti Didattici - Lezione Activity e Risorse Android

Questo documento riepilogativa i commenti didattici aggiunti ai codici, basandosi sulla **lezione PDF su Activity e Risorse Android** (Programmazione di Dispositivi Mobili).

---

## 📋 File Commentati

### 1. **ColorActivity.kt**

#### Concetti Trattati:

**Ciclo di Vita dell'Activity (Slide 3)**
- `onCreate()` → `onStart()` → `onResume()` → `onPause()` → `onStop()` → `onDestroy()`
- Spiegazione di quando vengono chiamati e che stato ha l'Activity in ogni fase
- Visualizzazione dello stack delle Activity (da slide 26)

**Intent Espliciti (Slide 6)**
- Definizione: "Un Intent esplicito indica in modo diretto il componente da avviare"
- Sintassi: `Intent(Context, Class)`
- Differenza tra Intent espliciti e impliciti (slide 5 e 10)

**Intent Extra - Passaggio Dati (Slide 9)**
- Come passare dati con `putExtra()` e `getStringExtra()`
- Elvis operator (`?:`) per valori di default

**Ritorno di Risultati (Slide 23-24)**
- `setResult(resultCode, resultIntent)` per ritornare dati
- `RESULT_OK` vs `RESULT_CANCELED`
- `finish()` per chiudere l'Activity

**State Management in Compose**
- `remember { mutableStateOf(...) }` per stato che persiste durante ricomposizioni
- `rememberSaveable` per stato che persiste anche attraverso ricreazioni dell'Activity
- Sintactic sugar `by` per delegazione

**Componenti Compose**
- `Scaffold`: struttura standard UI
- `Column` e `Row`: contenitori verticali e orizzontali
- `TextField`: input testuale con colori custom
- `Box`: contenitore semplice
- `Button`: pulsante con callback

---

### 2. **MainActivity.kt**

#### Concetti Trattati:

**ActivityResultLauncher (Android 11+)**
- Nuovo sistema per ricevere risultati da altre Activity
- Alternativa a `startActivityForResult()` e `onActivityResult()` (deprecated)
- `registerForActivityResult()` con `ActivityResultContracts.StartActivityForResult()`

**Ciclo di Vita Completo**
- Spiegazione dettagliata di ogni metodo:
  - `onCreate()`: inizializzazione UNA SOLA VOLTA
  - `onStart()`: Activity diventa visibile
  - `onResume()`: Activity riceve focus (stato "visible" in slide 3)
  - `onPause()`: Activity perde focus (stato "partially visible")
  - `onStop()`: Activity non più visibile (stato "hidden")
  - `onDestroy()`: pulizia e distruzione

**Task e Back Stack (Slide 25-26)**
- Spiegazione dello stack delle Activity
- Come MainActivity lancia ColorActivity
- Come il BACK button funziona

**State Management - remember vs rememberSaveable**
- `remember`: stato perso durante ricreazioni (rotazione schermo)
- `rememberSaveable`: stato salvato e ripristinato (Bundle)
- Quando usare quale

**Composable MainScreen**
- Uso di stato per la nota (TextField)
- Cambio dinamico del colore di background
- Layout con Compose (Scaffold, Column, Row)
- Callback per aprire ColorActivity

---

### 3. **AndroidManifest.xml**

#### Concetti Trattati:

**Struttura del Manifest**
- Ruolo del file AndroidManifest.xml nell'app Android
- Dichiarazioni di: Activity, Service, Broadcast Receiver, Content Provider, Permessi

**Activity Declaration**
- `<activity>` per dichiarare le Activity dell'app
- Attributi importanti: `android:name`, `android:exported`, `android:label`, `android:theme`

**Intent Filter (Slide 18)**
- Dichiarazione di quali Intent un componente può ricevere
- Tre elementi del filtro:
  - `<action>`: l'azione richiesta
  - `<category>`: il contesto
  - `<data>`: URI e MIME type (opzionale)

**Azioni Standard (Slide 11)**
- `ACTION_MAIN`: Activity principale
- `ACTION_VIEW`, `ACTION_EDIT`, `ACTION_SEND`: altre azioni comuni

**Categorie Standard (Slide 13)**
- `CATEGORY_LAUNCHER`: appare nel launcher (menu app)
- `CATEGORY_DEFAULT`, `CATEGORY_BROWSABLE`, `CATEGORY_HOME`: altre categorie

**Exported Attribute**
- `android:exported="true"`: altre app possono lanciare questa Activity
- `android:exported="false"`: Activity interna, non visibile all'esterno

---

## 🎯 Mappa dei Concetti dalle Slide

### Slide 2-3: Activity
- ✅ Ciclo di vita (onCreate → onDestroy)
- ✅ Stati visibili in ColorActivity.kt

### Slide 4-9: Intent
- ✅ Intent espliciti e impliciti
- ✅ Extra (putExtra, getStringExtra)
- ✅ Passaggio dati tra Activity

### Slide 10-13: Intent Impliciti
- ✅ Action, Category, Data
- ✅ Intent Filter dichiarato in Manifest

### Slide 18-20: Intent Filter
- ✅ Dichiarazioni nel Manifest.xml
- ✅ Risoluzione degli Intent

### Slide 23-24: Activity che Restituiscono Valori
- ✅ startActivityForResult / ActivityResultLauncher
- ✅ setResult() e onActivityResult()
- ✅ Ritorno di dati tra Activity

### Slide 25-26: Task
- ✅ Stack di Activity
- ✅ Back button behavior

### Risorse Android (Slide 27-45)
- ✅ String resources (@string/app_name)
- ✅ Colori (@color)
- ✅ Dimension (@dimen)
- ✅ Drawable resources

---

## 💡 Come Usare Questi Commenti

1. **Studio**: Leggi i commenti insieme alla lezione PDF per comprensione approfondita
2. **Pratica**: Segui i commenti mentre scrivi codice simile
3. **Riferimento**: Ritorna ai commenti quando hai domande su Activity, Intent, State

---

## 🔗 Correlazioni con le Slide

| Slide | Argomento | File | Righe |
|-------|-----------|------|-------|
| 2-3 | Activity lifecycle | MainActivity.kt | 181-265 |
| 4-9 | Intent basics | ColorActivity.kt | 32-62 |
| 6 | Intent espliciti | ColorActivity.kt | 67-84 |
| 9 | Passaggio dati | ColorActivity.kt | 67-84 |
| 18 | Intent Filter | AndroidManifest.xml | 47-80 |
| 23-24 | Activity results | MainActivity.kt | 70-117 |
| 25-26 | Tasks | MainActivity.kt | 127-137 |

---

## 📚 Risorse Aggiuntive

- Documentazione Compose: `@Composable`, `remember`, `rememberSaveable`
- Documentazione Android Activity: ciclo di vita, Intent
- ActivityResultContracts: API moderna per risultati

---

**Creato per**: Corso di Programmazione Dispositivi Mobili 2026
**Basato su**: Lezione Activity e Risorse Android (PDF fornito)
**Linguaggio**: Kotlin + Compose + XML

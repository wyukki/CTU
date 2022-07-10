**OMO SEMESTRAL PROJECT** by Volodymyr Semenyug and Roman Danilchenko

**Smart Home.**

**Zadáni**
Vytvořit aplikaci pro virtuální simulaci inteligentního domu, kde simulujeme chod domácnosti, používáme jednotlivá zařízení domu a vyhodnocujeme využití, spotřebu, volný a pracovní čas jednotlivých osob.

**Funkční požadavky**

F1.	Entity se kterými pracujeme je dům, okno (+ venkovní žaluzie), patro v domu, senzor, zařízení (=spotřebič), osoba, auto, kolo, domácí zvíře jiného než hospodářského typu, plus libovolné další entity. **DONE.**

F2.	Jednotlivá zařízení v domu mají API na ovládání. Zařízení mají stav, který lze měnit pomocí API na jeho ovládání. Akce z API jsou použitelné podle stavu zařízení. Vybraná zařízení mohou mít i obsah - lednice má jídlo, CD přehrávač má CD. **DONE.** API's for appliances can be found in package main.appliances.API.

F3.	Spotřebiče mají svojí spotřebu v aktivním stavu, idle stavu, vypnutém stavu. **DONE.** Implemented in Appliance class.

F4.	Jednotlivá zařízení mají API na sběr dat o tomto zařízení. O zařízeních sbíráme data jako spotřeba elektřiny, plynu, vody a funkčnost (klesá lineárně s časem). **HALF DONE.** Implemented in Appliance class.

F5.	Jednotlivé osoby a zvířata mohou provádět aktivity(akce), které mají nějaký efekt na zařízení nebo jinou osobu.
**DONE.** People can change appliance state, walk with pets, feed pets etc. Implemented in classes PeopleWithApplianceActivityHandler,  PeopleLifeActivityHandler, used in Simulation class.

F6.	Jednotlivá zařízení a osoby se v každém okamžiku vyskytují v jedné místnosti (pokud nesportují) a náhodně generují eventy (eventem může být důležitá informace a nebo alert). **DONE.** Implemented in classes PeopleWithApplianceActivityHandler,  PeopleLifeActivityHandler, used in Simulation class.

F7.	Eventy jsou přebírány a odbavovány vhodnou osobou (osobami) nebo zařízením. **DONE.** Events are all written in Simulation class.

F8.	Vygenerování reportů. **DONE.** Reports are generated to report.txt. House configuration report is located in package main.reports. Activity and usage reports are implemented in PeopleWithApplianceActivityHandler,  PeopleLifeActivityHandler classes.

F9.	Při rozbití zařízení musí obyvatel domu prozkoumat dokumentaci k zařízení - najít záruční list, projít manuál na opravu a provést nápravnou akcí. **HALF DONE.** Only father can read manual and repair appliances. Father has list of broken appliance, and he is notified if appliance is broken.

F10.	Rodina je aktivní a volný čas tráví zhruba v poměru (50% používání spotřebičů v domě a 50% sport kdy používá sportovní náčiní kolo nebo lyže). Když není volné zařízení nebo sportovní náčiní, tak osoba čeká. **DONE.** 

**Nefunkční požadavky.**
1. Reporty jsou generovány do textového souboru. **DONE.** Generated to report.txt.
2. Konfigurace domu, zařízení a obyvatel domu může být nahrávána přímo z třídy nebo externího souboru (preferován je json). **NOT DONE.** For now json is not created.

**Used design patterns.**
1. Factory method. Creation of appliances depending on their energy type. Implemented in package main.appliances.creators.
2. Builders. Multiple builders for creation of rooms, floors and house. Implemented in packages main.building.floorBuilders, main.building.roomBuilders, main.building.houseBuilder.
3. Lazy initialization. Creation of manual when it is needed. Implemented in Appliance class.
4. State machine. Used to represent states of appliances and pets. Implemented in packages main.appliances.states, main.pets.states.
5. Observer. Used to notify father when appliance is broken. ApplianceEventHandler is Observer (main.event), Appliance is Observable(main.appliances).
6. Singleton. Used to create a Car. Implemented in Car class(main.activityElements).

**Požadované výstupy.**
1. Design ve formě use case diagramů, class diagramů a stručného popisu jak chcete úlohu realizovat. 
**DONE.** Class and Use case diagrams can be found in package diagrams.
2. Veřejné API - Javadoc vygenerovaný pro funkce, kterými uživatel pracuje s vaším software. **DONE.**  
3. Dvě různé konfigurace domu a pro ně vygenerovány reporty za různá období. Minimální konfigurace alespoň jednoho domu je: 6 osob, 3 zvířata, 8 typů spotřebičů, 20 ks spotřebičů, 6 místností, jedny lyže, dvě kola. **DONE.** Winter house and summer house configuration.


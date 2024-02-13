## Liste des enseignants :
```bash
curl -X GET "https://edt-api.univ-avignon.fr/api/enseignants" \
     -H "Accept: application/json, text/plain, */*" \
     -H "Accept-Encoding: gzip, deflate, br" \
     -H "Accept-Language: fr-FR,fr;q=0.9" \
     -H "Connection: keep-alive" \
     -H "Host: edt-api.univ-avignon.fr" \
     -H "Origin: https://edt.univ-avignon.fr" \
     -H "Referer: https://edt.univ-avignon.fr/" \
     -H "Sec-Fetch-Dest: empty" \
     -H "Sec-Fetch-Mode: cors" \
     -H "Sec-Fetch-Site: same-site" \
     -H "token:{SESSION_TOKEN}"
```

**Exemple de sortie :**

```json
{
    "results": [
        {
            "letter": "A",
            "names": [
                {
                    "name": "XXXXXXX XXXX",
                    "code": "00",
                    "uapvRH": "000",
                    "searchString": "XXXXX XXXX"
                },
                {
                    "name": "YYYYYY YYYYY",
                    "code": "1111",
                    "uapvRH": "1111",
                    "searchString": "YYYY YYYYYY"
                },
                ...
            ]
        }
    ]
}
```

## Liste des formations :
```bash
curl -X GET "https://edt-api.univ-avignon.fr/api/elements" \
     -H "Accept: application/json, text/plain, */*" \
     -H "Accept-Encoding: gzip, deflate, br" \
     -H "Accept-Language: fr-FR,fr;q=0.9" \
     -H "Connection: keep-alive" \
     -H "Host: edt-api.univ-avignon.fr" \
     -H "Origin: https://edt.univ-avignon.fr" \
     -H "Referer: https://edt.univ-avignon.fr/" \
     -H "Sec-Fetch-Dest: empty" \
     -H "Sec-Fetch-Mode: cors" \
     -H "Sec-Fetch-Site: same-site" \
     -H "token:{SESSION_TOKEN}"
```

**Exemple de sortie :**
```json
{
    "results": [
        {
            "letter": null,
            "names": [
                {
                    "name": "# outils numeriques - l2",
                    "code": "HM-BEE-003",
                    "searchString": "# outils numeriques - l2"
                },
                {
                    "name": "dut genie biologique s1",
                    "code": "HM-IUT-001",
                    "searchString": "dut genie biologique s1"
                },
                ...
            ]
        },
        {
            "letter": "STAPS",
            "names": [
                {
                    "name": "hdr sciences et techn.  activites. phys. et  sportives",
                    "code": "2-HD25",
                    "searchString": "STAPShdr sciences et techn.  activites. phys. et  sportives"
                },
                ...
            ]
        }
    ]
}
```

## Emploie du temps format Json :

### Par formation :
```bash
curl -X GET "https://edt-api.univ-avignon.fr/api/events_promotion/{CODE_FORMATION}" \
     -H "Accept: application/json, text/plain, */*" \
     -H "Accept-Encoding: gzip, deflate, br" \
     -H "Accept-Language: fr-FR,fr;q=0.9" \
     -H "Connection: keep-alive" \
     -H "Host: edt-api.univ-avignon.fr" \
     -H "Origin: https://edt.univ-avignon.fr" \
     -H "Referer: https://edt.univ-avignon.fr/" \
     -H "Sec-Fetch-Dest: empty" \
     -H "Sec-Fetch-Mode: cors" \
     -H "Sec-Fetch-Site: same-site" \
     -H "token:{SESSION_TOKEN}"
```
**Exemple de sortie :**

```json
{
    "results": [
        {
            "code": "HM-IUT-001",
            "start": "2023-11-01T00:00:00+01:00",
            "end": "2023-11-02T00:00:00+01:00",
            "title": "Férié",
            "type": "",
            "memo": null,
            "favori": ""
        },
        ...
    ],
    "hashUrl": "def502002e95fcac21849070131680a1b52c6d1490b321dc4cd63edade43bd28b9f55a1ab8878b199915aefddba58d295c1e2b61ac5742358683c1de0044eb002d9306204916c2157eb762bf96ab1121980223fbf7c0b19d3a19e2d08297aa00305d2381a8201a95b63d461113d18c916ea14a4a38cb658a856e9a5f6e2e0b2c8d12e093295c4f1964ece89405de3b"
}
```
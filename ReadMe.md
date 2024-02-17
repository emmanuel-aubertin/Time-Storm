# UAPV EDT API

## How to Get Your Token:

You must already have a university account. If you have one, you just need to log in using this [link](https://cas.univ-avignon.fr/cas/login?service=https://edt.univ-avignon.fr/login).

Once this is done, just follow these steps:
Right-click -> Inspect -> Storage -> Session Storage -> token


## Get All University Teachers:
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

**JSON Example:**

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

## Get All Educational Programs (Year, Specialty...):
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

**JSON Example:**

```json
{
    "results": [
        {
            "letter": null,
            "names": [
                {
                    "name": "# digital tools - l2",
                    "code": "HM-BEE-003",
                    "searchString": "# digital tools - l2"
                },
                {
                    "name": "dut biomedical engineering s1",
                    "code": "HM-IUT-001",
                    "searchString": "dut biomedical engineering s1"
                },
                ...
            ]
        },
        {
            "letter": "STAPS",
            "names": [
                {
                    "name": "hdr sciences and tech. physical act. and sports",
                    "code": "2-HD25",
                    "searchString": "STAPShdr sciences and tech. physical act. and sports"
                },
                ...
            ]
        }
    ]
}
```

## Get classroom list :
```bash 
curl -X GET "https://edt-api.univ-avignon.fr/api/salles" \
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

**JSON Example:**

```json

{
    "results": [
        {
            "letter": "Agrosciences",
            "names": [
                {
                    "name": "a002 (cmi) v",
                    "code": "AGR_A002",
                    "searchString": "a002(cmi)vAgrosciencesa002 (cmi) v"
                },
                {
                    "name": "a015 salle info libre acces",
                    "code": "AGR_A015",
                    "searchString": "a015salleinfolibreaccesAgrosciencesa015 salle info libre acces"
                },
                ...
            ]
        }
    ]
}
```

## Get Specific Schedule:

### By Education Path:
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
**JSON Example:**

```json
{
    "results": [


        {
            "code": "HM-IUT-001",
            "start": "2023-11-01T00:00:00+01:00",
            "end": "2023-11-02T00:00:00+01:00",
            "title": "Holiday",
            "type": "",
            "memo": null,
            "favorite": ""
        },
        ...
    ],
    "hashUrl": "def502002e95fcac21849070131680a1b52c6d1490b321dc4cd63edade43bd28b9f55a1ab8878b199915aefddba58d295c1e2b61ac5742358683c1de0044eb002d9306204916c2157eb762bf96ab1121980223fbf7c0b19d3a19e2d08297aa00305d2381a8201a95b63d461113d18c916ea14a4a38cb658a856e9a5f6e2e0b2c8d12e093295c4f1964ece89405de3b"
}
```

### By Education classroom:

```bash
curl -X GET "https://edt-api.univ-avignon.fr/api/events_salle/{SALLE_CODE}" \
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

**JSON Example:**

```json
{
    "results": [
        {
            "code": null,
            "start": "2023-09-14T08:00:00+00:00",
            "end": "2023-09-14T10:00:00+00:00",
            "title": "Matière : PREPARATION AU PROJET PROFESS\nPromotion : M1 HYDROGEOLOGIE, SOL ET ENVIRONNEMENT (HSE)\nSalle : A016 V ( GT )\nType : CM/TD\nMémo : Maison de l'eau\n",
            "type": "CM",
            "memo": null,
            "favori": ""
        },
        {
            "code": null,
            "start": "2023-09-21T08:00:00+00:00",
            "end": "2023-09-21T11:00:00+00:00",
            "title": "Matière : PREPARATION AU PROJET PROFESS\nPromotion : M1 SCIENCES ET DURABILITE DES PRODUCTIONS VEGETALES\nSalle : A016 V ( GT )\nType : CM/TD\nMémo : Rencontre professionnels\n",
            "type": "CM",
            "memo": null,
            "favori": ""
        },
        ...
    ]
}
```
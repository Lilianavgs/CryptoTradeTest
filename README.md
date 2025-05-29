# CryptoTradeTest
Cryptocurrency exchange platform

---

## 🗂️ Esquema básico de la base de datos

- **crypto_coin**: tabla que almacena criptomonedas (id, symbol, name, description, created_at).
- **currency**: tabla que almacena monedas fiat o monedas (id, symbol, name, country, created_at).
- **crypto_coin_currency**: tabla intermedia que relaciona criptomonedas con monedas (crypto_coin_id, currency_id).

---

## 📚 Documentación adicional

- Para más detalles, revisa la carpeta `/database` donde se encuentra el backup SQL y el diagrama ER.
- Puedes importar el perfil de Postman desde `/postman/CryptoTrade.postman_collection.json` para probar la API fácilmente.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor abre un issue o pull request para sugerir mejoras o reportar errores.

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT.

---

*¡Gracias por usar CryptoTrade API!*

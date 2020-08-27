const jsonServer = require('json-server')
const server = jsonServer.create()
const middlewares = jsonServer.defaults()

server.use(middlewares)

server.get('/success', (req, res) => {
    res.status(200).jsonp({
        status: "success",
        value: {
            id: 90,
            joke: "Chuck Norris always knows the EXACT location of Carmen SanDiego.",
            categories: []
        }
    })
})

server.get('/emoji', (req, res) => {
    res.status(200).jsonp({
        emojis: "🧑‍🦰🧑🏻‍🦰🧑🏼‍🦰🧑🏽‍🦰🧑🏾‍🦰🧑🏿‍🦰🧑‍🦱🧑🏻‍🦱🧑🏼‍🦱🧑🏽‍🦱🧑🏾‍🦱🧑🏿‍🦱‍🦳🧑🏻‍🦳🧑🏼‍🦳🧑🏽‍🦳🧑🏾‍🦳🧑🏿‍🦳🧑‍🦲🧑🏻‍🦲🧑🏼‍🦲🧑🏽‍🦲🧑🏾‍🦲🧑🏿‍🦲🧑‍⚕️🧑🏻‍⚕️🧑🏼‍⚕️🧑🏽‍⚕️🧑🏾‍⚕️🧑🏿‍⚕🧑‍🎓🧑🏻‍🎓🧑🏼‍🎓🧑🏽‍🎓🧑🏾‍🎓🧑🏿‍🎓 ‍🏫🧑🏻‍🏫🧑🏼‍🏫🧑🏽‍🏫🧑🏾‍🏫🧑🏿‍🏫 " +
            "🧑‍⚖️🧑🏻‍⚖️🧑🏼‍⚖️🧑🏽‍⚖️🧑🏾‍⚖️🧑🏿‍⚖️ 🧑‍🌾🧑🏻‍🌾🧑🏼‍🌾🧑🏽‍🌾🧑🏾‍🌾🧑🏿‍🌾 🧑‍🍳🧑🏻‍🍳🧑🏼‍🍳🧑🏽‍🍳🧑🏾‍🍳🧑🏿‍🍳 🧑‍🔧🧑🏻‍🔧🧑🏼‍🔧🧑🏽‍🔧🧑🏾‍🔧🧑🏿‍🔧 " +
            "🧑‍🏭🧑🏻‍🏭🧑🏼‍🏭🧑🏽‍🏭🧑🏾‍🏭🧑🏿‍🏭 🧑‍💼🧑🏻‍💼🧑🏼‍💼🧑🏽‍💼🧑🏾‍💼🧑🏿‍💼 🧑‍🔬🧑🏻‍🔬🧑🏼‍🔬🧑🏽‍🔬🧑🏾‍🔬🧑🏿‍🔬 🧑‍💻🧑🏻‍💻🧑🏼‍💻🧑🏽‍💻🧑🏾‍💻🧑🏿‍💻 🧑‍🎤🧑🏻‍🎤🧑🏼‍🎤🧑🏽‍🎤🧑🏾‍🎤🧑🏿‍🎤 🧑‍🎨🧑🏻‍🎨🧑🏼‍🎨🧑🏽‍🎨🧑🏾‍🎨🧑🏿‍🎨 🧑‍✈️🧑🏻‍✈️🧑🏼‍✈️🧑🏽‍✈️🧑🏾‍✈️🧑🏿‍✈️" +
            "🧑‍🚀🧑🏻‍🚀🧑🏼‍🚀🧑🏽‍🚀🧑🏾‍🚀🧑🏿‍🚀 🧑‍🚒🧑🏻‍🚒🧑🏼‍🚒🧑🏽‍🚒🧑🏾‍🚒🧑🏿‍🚒 🤵‍♂️🤵🏻‍♂️🤵🏼‍♂️🤵🏽‍♂️🤵🏾‍♂️🤵🏿‍♂️ 🤵‍♀️🤵🏻‍♀️🤵🏼‍♀️🤵🏽‍♀️🤵🏾‍♀️🤵🏿‍♀️ 👰‍♂️👰🏻‍♂️👰🏼‍♂️👰🏽‍♂️👰🏾‍♂️👰🏿‍♂️" +
            "👰‍♀️👰🏻‍♀️👰🏼‍♀️👰🏽‍♀️👰🏾‍♀️👰🏿‍♀️ 👩‍🍼👩🏻‍🍼👩🏼‍🍼👩🏽‍🍼👩🏾‍🍼👩🏿‍🍼 👨‍🍼👨🏻‍🍼👨🏼‍🍼👨🏽‍🍼👨🏾‍🍼👨🏿‍🍼 🧑‍🍼🧑🏻‍🍼🧑🏼‍🍼🧑🏽‍🍼🧑🏾‍🍼🧑🏿‍🍼 " +
            "🧑‍🎄🧑🏻‍🎄🧑🏼‍🎄🧑🏽‍🎄🧑🏾‍🎄🧑🏿‍🎄 🐈‍⬛ 🐻‍❄️ 🏳️‍⚧🌏️"
    })
})

server.get('/200/failed', (req, res) => {
    res.status(200).jsonp({
        status: "failed",
        error: "foo"
    })
})

server.get('/200/success', (req, res) => {
    res.status(200).jsonp({
        status: "success",
        value: {
            id: 90,
            joke: "Chuck Norris always knows the EXACT location of Carmen SanDiego.",
            categories: []
        }
    })
})

server.get('/failed', (req, res) => {
    res.status(500).jsonp({
        status: "failed",
        value: {
            id: 90,
            joke: "Chuck Norris always knows the EXACT location of Carmen SanDiego.",
            categories: []
        }
    })
})

server.get('/notfound', (req, res) => {
    res.status(404).jsonp({
        status: "failed"
    })
})

server.listen(3289, () => {
    console.log('JSON Server is running')
})
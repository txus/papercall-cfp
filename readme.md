# papercall-cfp voice-activated Chrome extension

## Usage

Install the chrome extension, go to any submission in Papercall and just say:

`"Yes" | "No" | "Maybe"`

To vote for that submission and go to the next one.

## Development

On one window:

```
lein fig
```

On the other:

```
lein content
```

Goto `chrome://extensions`, activate developer mode and `Load unpacked` the
`resources/unpacked` folder. Every time you change and figwheel recompiles,
refresh the extension here.

## Release

```
lein release
./scripts/package.sh
```

# Directory Documentation for `cheneer`

## Description
This package provides classes used to manage [Chen Enhanced or Extended Entity Relationship (EER) Diagrams)](https://plantuml.com/er-diagram).

## Link
- [Enhanced or Extended Entity Relationship (EER) Diagrams (with Chen's notation)](https://plantuml.com/er-diagram)

## Experimental compact attribute notation

Compact attribute rendering is an experimental, partial first phase of
[#2812](https://github.com/plantuml/plantuml/issues/2812), related to the broader
Chen notation discussion in [#2755](https://github.com/plantuml/plantuml/issues/2755).
Enable it before the first entity or relationship declaration:

```plantuml
@startchen
left to right direction
notation compact

entity account {
  id <<key>>
  profile {
    display_name
  }
  roles <<multi>>
  age <<derived>>
}

entity session <<weak>> {
  token <<discriminator>>
}

relationship opens <<identifying>> {
  time
  agent
}

account -1- opens
opens =N= session
@endchen
```

In this profile, entity attributes are rows inside their entity and relationship
attributes share one rectangular box connected to the relationship. Composite
attributes are indented. The exact, case-insensitive stereotype labels `key`,
`discriminator`, `multi`, and `derived` add compact decorations; other labels
have no compact semantic effect. This phase does not change cardinality,
participation, specialization, or other Chen behavior. Standard `@startchen`
diagrams remain unchanged when `notation compact` is absent.

## Reference
- [Enhanced entity–relationship model _(on Wikipedia)_](https://en.wikipedia.org/wiki/Enhanced_entity%E2%80%93relationship_model)
- [Entity–relationship model _(on Wikipedia)_](https://en.wikipedia.org/wiki/Entity%E2%80%93relationship_model)

## Credit
- Thank to the contribution of :octocat: [@Benjamin-Davies](https://github.com/Benjamin-Davies)

## Misc.
- [Peter Chen _(on Wikipedia)_](https://en.wikipedia.org/wiki/Peter_Chen)

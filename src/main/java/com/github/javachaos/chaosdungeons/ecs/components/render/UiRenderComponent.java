package com.github.javachaos.chaosdungeons.ecs.components.render;

public abstract class UiRenderComponent extends RenderComponent {
    //TODO Fix the rendering design flaw.
    // currently the SpriteComponent will have it's render method called
    // but not as a RenderComponent so the nessicary bind and unbind / transformations
    // do not get applied, this is the main problem!
    // As you can see with DebugCollisionRenderer
    // we need to make a good design choice here.

    // TODO ***research this topic for a good design pattern.***
}

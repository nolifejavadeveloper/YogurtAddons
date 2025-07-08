package net.ethann.yogurtaddons.feature.impl.redstonetrigger.trigger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.util.BlockPos;

@RequiredArgsConstructor
@Getter
@Setter
public class Trigger {
    private final BlockPos blockPos;
    private final String name;
    private final TriggerType type;
    private transient boolean ready = true;
}

package net.aoba.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.aoba.Aoba;
import net.aoba.event.events.RightMouseDownEvent;
import net.aoba.event.listeners.RightMouseDownListener;
import net.aoba.module.Module;
import net.aoba.settings.types.FloatSetting;
import net.aoba.settings.types.KeybindSetting;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public class ClickTP extends Module implements RightMouseDownListener {

	private FloatSetting distance;
	
	public ClickTP() {
		super(new KeybindSetting("key.clicktp", "ClickTP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("ClickTP");
		this.setCategory(Category.Movement);
		this.setDescription("Allows the user to teleport where they are looking.");
		
		distance = new FloatSetting("clicktp.distance", "Max Distance", "Max Distance to teleport.", 10.0f, 1.0f, 200.0f, 1.0f);
		
		this.addSetting(distance);
	}

	@Override
	public void onDisable() {
		Aoba.getInstance().eventManager.RemoveListener(RightMouseDownListener.class, this);
	}

	@Override
	public void onEnable() {
		Aoba.getInstance().eventManager.AddListener(RightMouseDownListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnRightMouseDown(RightMouseDownEvent event) {
		HitResult raycast = MC.player.raycast(distance.getValue(), MC.getRenderTickCounter().getTickDelta(false), false);
        
        if (raycast.getType() == HitResult.Type.BLOCK) {
        	BlockHitResult raycastBlock = (BlockHitResult) raycast;
            BlockPos pos = raycastBlock.getBlockPos();
            Direction side = raycastBlock.getSide();

            Vec3d newPos = new Vec3d(pos.getX() + 0.5 + side.getOffsetX(), pos.getY() + 1, pos.getZ() + 0.5 + side.getOffsetZ());
            int packetsRequired = (int) Math.ceil(MC.player.getPos().distanceTo(newPos) / 10) - 1;

            for (int i = 0; i < packetsRequired; i++) {
                MC.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            }

            MC.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(newPos.x, newPos.y, newPos.z, true));
            MC.player.setPosition(newPos);
        }
	}
}

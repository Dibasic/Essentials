package com.Da_Technomancer.essentials.blocks.redstone;

import com.Da_Technomancer.essentials.EssentialsConfig;
import com.Da_Technomancer.essentials.tileentities.CircuitTileEntity;
import com.Da_Technomancer.essentials.tileentities.ConstantCircuitTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ConsCircuit extends AbstractCircuit{

	public ConsCircuit(){
		super("cons_circuit");
	}

	@Override
	public boolean useInput(CircuitTileEntity.Orient or){
		return false;
	}

	@Override
	public float getOutput(float in0, float in1, float in2, CircuitTileEntity te){
		if(te instanceof ConstantCircuitTileEntity){
			return ((ConstantCircuitTileEntity) te).setting;
		}

		return 0;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit){
		TileEntity te;
		if(EssentialsConfig.isWrench(playerIn.getHeldItem(hand))){
			super.onBlockActivated(state, worldIn, pos, playerIn, hand, hit);
		}else if(!worldIn.isRemote && (te = worldIn.getTileEntity(pos)) instanceof ConstantCircuitTileEntity){
			NetworkHooks.openGui((ServerPlayerEntity) playerIn, (ConstantCircuitTileEntity) te, buf -> {buf.writeFloat(((ConstantCircuitTileEntity) te).setting); buf.writeString(((ConstantCircuitTileEntity) te).settingStr); buf.writeBlockPos(pos);});
		}

		return true;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn){
		return new ConstantCircuitTileEntity();
	}
}

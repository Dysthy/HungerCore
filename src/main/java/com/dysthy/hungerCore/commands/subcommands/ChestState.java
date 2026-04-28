package com.dysthy.hungerCore.commands.subcommands;
enum ChestState {
   ADD,
   REMOVE;
   
   private static ChestState[] $values() {
      return new ChestState[]{ADD, REMOVE};
   }
}

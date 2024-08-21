"use client";
import React from "react";
import Image from "next/image";
import Logo from "@/public/Logo.png";
import NewTask from "./NewTask";
import { Avatar, Input } from "@nextui-org/react";
import { Task } from "./TaskCard";
interface NavBarParams {
  search: string;
  setSearch: (value: string) => void;
  taskList : Task[] | undefined
}
function NavBar({ search, setSearch,taskList }: NavBarParams) {
  return (
    <header className="flex justify-between px-12 py-3 items-center  shadow-lg sticky top-0 z-50 bg-white">
      <Image alt="logo" src={Logo} height={24} />
      <Input
        variant="bordered"
        label="search"
        className="w-[25%] h-12"
        value={search}
        onValueChange={(newVal) => setSearch(newVal)}
      />
      <NewTask taskList={taskList} />
      {/* <div className="flex justify-around gap-4 items-center">
        <Avatar src="https://i.pravatar.cc/150?u=a04258a2462d826712d" />
        <div className="flex-col text-sm">
          <div className="text-primaryText">Ahmed Chennaoui</div>
          <div className="text-secondaryText">Administrator</div>
        </div>
      </div> */}
    </header>
  );
}

export default NavBar;

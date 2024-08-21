"use client";
import NewTask from "@/components/NewTask";
import { TaskCard } from "@/components/TaskCard";
import backend from "@/api/backend";
import { Task } from "@/components/TaskCard";
import { useQuery } from "react-query";
import { useState } from "react";
import NavBar from "@/components/NavBar";

const fetchTasks = async (): Promise<Task[]> => {
  const response = await backend.get("/tasks");
  return response.data;
};

export default function Home() {
  const [search, setSearch] = useState("");
  const { data, error, isLoading } = useQuery(["fetch_tasks"], fetchTasks, {
    refetchInterval: 1000,
    refetchOnWindowFocus: false,
  });

  return (
    <>
      <NavBar search={search} setSearch={setSearch} taskList={data} />
      <main className="p-8 mx-auto flex gap-y-5 gap-x-[2.5%] flex-wrap">
        {data
          ?.filter((task) =>
            task.task_name.toLowerCase().includes(search.toLowerCase())
          )
          .sort((taskA, taskB) => taskB.created_at - taskA.created_at)
          .map((task) => (
            <TaskCard key={task.task_id} task={task} taskList={data} />
          ))}
      </main>
    </>
  );
}

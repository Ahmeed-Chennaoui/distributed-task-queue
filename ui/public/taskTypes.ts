import MLLogo from "@/public/learning-machine-ai-icon.svg"
import SparkLogo from "@/public/apache_spark-icon.svg"
import MathLogo from "@/public/math.png"
import SecurityLogo from "@/public/security2.png"
export interface TaskParams<T> {
  [key: string]: T | T[];
}

interface TaskType {
  label: string;
  value: string;
  paramsInterface: TaskParams<string | number | object>;
}

export const taskTypes: TaskType[] = [
  {
    label: "Spark ETL",
    value: "Spark ETL",
    paramsInterface: {
      "Data Link": "",
      "Batch Size": 0,
      "Result Link": "",
    },
  },
  {
    label: "ML Training",
    value: "ML Training",
    paramsInterface: {
      "Training Data": "",
      Epochs: 0,
    },
  },
  {
    label: "First prime larger than n",
    value:"first_prime_larger_than_n",
    paramsInterface: {
      "n" :"0"
    }
  }, 
  {
    label: "Benchmark cryptographic performance of primes",
    value:"benchmark_primes",
    paramsInterface: {
      "p" :"0",
      "q" : "0"
    }
  },

];

export const taskTypesLogos : Record<string,any> = {
  "Spark ETL" : SparkLogo,
  "test": MLLogo,
  "long":SparkLogo,
  "fail":SparkLogo,
  "ML Training": MLLogo,
  "first_prime_larger_than_n": MathLogo,
  "benchmark_primes": SecurityLogo

}


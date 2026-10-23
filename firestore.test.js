const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read notes", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("notes").get());
});

test("Authenticated user: can create own valid note", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const noteRef = aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_1");
  await assertSucceeds(noteRef.set({
    userId: ALICE_UID,
    title: "యోహాను సువార్త ధ్యానం",
    content: "దేవుడు లోకమును ఎంతో ప్రేమించెను...",
    bookName: "యోహాను",
    chapter: 3,
    verse: 16,
    tags: ["ప్రేమ", "నిత్యజీవము"],
    createdAt: new Date(),
  }));
});

test("Cross-user access: Alice cannot read Bob's notes", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(BOB_UID).collection("notes").doc("bob_note").set({
      userId: BOB_UID,
      title: "Bob's Note",
      content: "Content",
      bookName: "కీర్తనలు",
      chapter: 23,
      verse: 1,
      createdAt: new Date(),
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).collection("notes").doc("bob_note").get());
});

test("Schema guard: rejects unknown ghost fields", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const noteRef = aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_ghost");
  await assertFails(noteRef.set({
    userId: ALICE_UID,
    title: "Test Note",
    content: "Body",
    bookName: "ఆదికాండము",
    chapter: 1,
    verse: 1,
    createdAt: new Date(),
    isAdmin: true, // Ghost field not in schema
  }));
});

test("Bookmark isolation: Alice can create own bookmark", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bmRef = aliceDb.collection("users").doc(ALICE_UID).collection("bookmarks").doc("bm_1");
  await assertSucceeds(bmRef.set({
    userId: ALICE_UID,
    bookId: "PSA",
    bookName: "కీర్తనలు",
    chapter: 23,
    verse: 1,
    text: "యెహోవా నా కాపరి; నాకు లేమి కలుగదు.",
    color: "#FFD54F",
    createdAt: new Date(),
  }));
});

test("Flashcards: Alice can create and update reviewCount", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const fcRef = aliceDb.collection("users").doc(ALICE_UID).collection("flashcards").doc("fc_1");
  const now = new Date();
  await assertSucceeds(fcRef.set({
    userId: ALICE_UID,
    frontPrompt: "యెహోవా నా కాపరి...",
    backAnswer: "నాకు లేమి కలుగదు. (కీర్తనలు 23:1)",
    scriptureRef: "కీర్తనలు 23:1",
    reviewCount: 0,
    mastered: false,
    createdAt: now,
  }));

  await assertSucceeds(fcRef.update({
    reviewCount: 1,
    mastered: true,
  }));
});
